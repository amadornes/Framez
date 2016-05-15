package com.amadornes.trajectory.movement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.amadornes.trajectory.Trajectory;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovementCallback;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.IMovingStructure;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryFeature;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.movement.MovingBlock.ScheduledTick;
import com.amadornes.trajectory.network.NetworkHandler;
import com.amadornes.trajectory.network.packet.PacketStartMoving;
import com.amadornes.trajectory.util.ChunkHelper;
import com.amadornes.trajectory.util.EventHelper;
import com.amadornes.trajectory.world.FakeWorld;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public final class MovingStructure implements IMovingStructure {

    private final UUID id;
    private final World world;
    private final BlockSet blocks;
    private final ITrajectory trajectory;
    private final Set<TrajectoryFeature> features;
    private final IMovementCallback callback;

    private int ticksMoved;

    private Set<TileMoving> placeholders = null;
    private Set<Chunk> chunks = new HashSet<Chunk>();

    private boolean needsReRender = true;
    private int renderList = -1;

    public MovingStructure(UUID id, BlockSet blocks, ITrajectory trajectory, Set<TrajectoryFeature> features, IMovementCallback callback) {

        this.id = id;
        this.world = blocks.getWorld();
        this.blocks = blocks;
        this.trajectory = trajectory;
        this.features = features;
        this.callback = callback;

        for (IMovingBlock b : blocks)
            ((MovingBlock) b).setStructure(this);
    }

    public UUID getId() {

        return id;
    }

    @Override
    public World getWorld() {

        return world;
    }

    @Override
    public World getFakeWorld() {

        return FakeWorld.getFakeWorld(this);
    }

    @Override
    public BlockSet getBlocks() {

        return blocks;
    }

    @Override
    public ITrajectory getTrajectory() {

        return trajectory;
    }

    @Override
    public int getTicksMoved() {

        return ticksMoved;
    }

    @SuppressWarnings("unchecked")
    public void startMoving_do() {

        if (getWorld().isRemote) {
            for (IMovingBlock bl : blocks)
                ((MovingBlock) bl).doNotificationStuff(bl.getPosition());
            return;
        }

        // Make a list with all the chunks
        for (IMovingBlock bl : blocks) {
            BlockPos pos = bl.getPosition();
            BlockPos fPos = trajectory.transformPos(pos);
            chunks.add(world.getChunkFromBlockCoords(pos.x, pos.z));
            chunks.add(world.getChunkFromBlockCoords(fPos.x, fPos.z));
        }

        // Events
        EventHelper.fireStructureMovementStartEvent(Phase.START, this, trajectory);
        for (IMovingBlock bl : blocks)
            EventHelper.fireBlockMovementStartEvent(Phase.START, bl, trajectory);

        // Get a snapshot of the blocks
        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).startMovingPre();

        long time = world.getTotalWorldTime();
        for (Chunk c : chunks) {
            List<NextTickListEntry> scheduledUpdates = world.getPendingBlockUpdates(c, false);
            if (scheduledUpdates == null)
                continue;
            for (NextTickListEntry update : scheduledUpdates) {
                MovingBlock b = (MovingBlock) blocks.find(update.xCoord, update.yCoord, update.zCoord);
                if (b != null) {
                    b.scheduleTick(update.priority, update.scheduledTime - time);
                    continue;
                }
                world.scheduleBlockUpdateWithPriority(update.xCoord, update.yCoord, update.zCoord, update.func_151351_a(),
                        (int) (update.scheduledTime - time), update.priority);
            }
        }

        // Actually move the blocks
        for (IMovingBlock bl : blocks)
            MovementManager.instance.startMoving((MovingBlock) bl, trajectory);

        // Tell the client to start moving
        NetworkHandler.instance().sendToAll(new PacketStartMoving(this));

        // Finish moving the blocks
        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).startMovingPost();

        // Add all the placeholders
        for (Entry<BlockPos, Boolean> e : getTrajectory().getPlaceholderPositions(getBlocks()).entrySet()) {
            if (e.getValue()
                    || world.getBlock(e.getKey().x, e.getKey().y, e.getKey().z).isReplaceable(world, e.getKey().x, e.getKey().y,
                            e.getKey().z)) {
                TileMoving te = new TileMoving(this);
                world.setBlock(e.getKey().x, e.getKey().y, e.getKey().z, Trajectory.moving, 0, 3);
                world.setTileEntity(e.getKey().x, e.getKey().y, e.getKey().z, te);
                registerPlaceholder(te);
            }
        }

        // Update all the chunks
        for (Chunk chunk : chunks) {
            ChunkHelper.updateChunk(chunk);
            chunk.setChunkModified();
        }

        // Events
        for (IMovingBlock bl : blocks)
            EventHelper.fireBlockMovementStartEvent(Phase.END, bl, trajectory);
        EventHelper.fireStructureMovementStartEvent(Phase.END, this, trajectory);
    }

    public void finishMoving_do() {

        if (getWorld().isRemote) {
            for (IMovingBlock bl : blocks)
                ((MovingBlock) bl).doNotificationStuff(trajectory.transformPos(bl.getPosition()));
            return;
        }

        FakeWorld.getFakeWorld(this);

        // Remove placeholders
        for (TileMoving te : placeholders) {
            getWorld().setBlockToAir(te.xCoord, te.yCoord, te.zCoord);
            getWorld().removeTileEntity(te.xCoord, te.yCoord, te.zCoord);
        }

        BlockSet transformed = new BlockSet();

        // Events
        EventHelper.fireStructureMovementFinishEvent(Phase.START, this, trajectory);
        for (IMovingBlock bl : blocks)
            if (EventHelper.fireBlockMovementFinishEvent(Phase.START, bl, trajectory))
                transformed.add(bl);

        // Go through all the placement passes
        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).finishMovingPre();
        for (IMovingBlock bl : transformed)
            trajectory.transformBlock(bl);
        for (IMovingBlock bl : blocks)
            MovementManager.instance.finishMoving((MovingBlock) bl, trajectory);

        // Events
        EventHelper.fireStructureMovementPreUpdateEvent(this, getTrajectory());

        // Block updates
        for (IMovingBlock bl : blocks)
            ((MovingBlock) bl).finishMovingPost();

        // Clear memory
        transformed.clear();

        // Schedule block ticks
        for (IMovingBlock bl : blocks) {
            BlockPos t = trajectory.transformPos(bl.getPosition());
            for (ScheduledTick tick : ((MovingBlock) bl).getScheduledTicks())
                world.scheduleBlockUpdateWithPriority(t.x, t.y, t.z, bl.getBlock(), (int) tick.getTime(), tick.getPriority());
        }

        // Update all the chunks
        for (Chunk chunk : chunks) {
            ChunkHelper.updateChunk(chunk);
            chunk.setChunkModified();
        }

        // Events
        for (IMovingBlock bl : blocks)
            EventHelper.fireBlockMovementFinishEvent(Phase.END, bl, trajectory);
        EventHelper.fireStructureMovementFinishEvent(Phase.END, this, trajectory);
    }

    @SuppressWarnings("unchecked")
    public void moveEntities() {

        World fw = FakeWorld.getFakeWorld(this);
        Set<Entity> entities = new HashSet<Entity>();
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();

        for (IMovingBlock block : blocks) {
            block.getBlock().addCollisionBoxesToList(
                    fw,
                    block.getPosition().x,
                    block.getPosition().y,
                    block.getPosition().z,
                    AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2, 1).offset(block.getPosition().x, block.getPosition().y,
                            block.getPosition().z), boxes, null);
            for (AxisAlignedBB aabb : boxes) {
                for (AxisAlignedBB box : trajectory.transformAABB(aabb, ticksMoved)) {
                    box.maxY += 0.25;
                    entities.addAll(world.getEntitiesWithinAABB(Entity.class, box));
                }
            }
            boxes.clear();
        }

        for (Entity entity : entities)
            trajectory.moveEntity(entity, ticksMoved);

        entities.clear();
    }

    @Override
    public void finishMoving() {

        while (trajectory.getProgress(ticksMoved) < 1)
            tick();
    }

    @Override
    public IMovementCallback getCallback() {

        return callback;
    }

    public void tick(Phase phase) {

        FakeWorld.getFakeWorld(this);

        if (phase == Phase.END)
            tick();
    }

    public void tick() {

        double progress = trajectory.getProgress(ticksMoved);

        if (progress >= 1)
            return;

        if (progress == 0) {
            startMoving_do();
            if (callback != null)
                callback.onStartMoving(this);
        }

        ticksMoved++;

        if (features.contains(TrajectoryFeature.MOVE_ENTITIES))
            moveEntities();

        if (trajectory.getProgress(ticksMoved) >= 1) {
            finishMoving_do();
            if (callback != null)
                callback.onFinishMoving(this);
        }
    }

    @Override
    public Set<TrajectoryFeature> getFeatures() {

        return features;
    }

    @Override
    public void scheduleReRender() {

        needsReRender = true;
    }

    public boolean needsReRender() {

        return renderList != -2 && needsReRender;
    }

    public int getRenderList() {

        return renderList;
    }

    public void setRenderList(int renderList) {

        this.renderList = renderList;
        needsReRender = false;
    }

    public Set<TileMoving> getPlaceholders() {

        return placeholders;
    }

    public void registerPlaceholder(TileMoving tile) {

        if (placeholders == null)
            placeholders = new HashSet<TileMoving>();
        placeholders.add(tile);
    }

    public TileMoving getRenderingTile() {

        if (placeholders.isEmpty())
            return null;
        return placeholders.iterator().next();
    }

    public MovingBlock addBlock(MovingBlock block) {

        blocks.add(block);
        block.setStructure(this);

        return block;
    }

}
