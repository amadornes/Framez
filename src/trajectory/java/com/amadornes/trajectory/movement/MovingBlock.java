package com.amadornes.trajectory.movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.trajectory.api.IExtraMovementData;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.util.BlockUtils;
import com.amadornes.trajectory.world.FakeWorld;

public final class MovingBlock implements IMovingBlock {

    private final World world;
    private final BlockPos position;

    private Block block = Blocks.stone;
    private int metadata;
    private TileEntity tileentity;
    private Map<String, IExtraMovementData> extraData = new HashMap<String, IExtraMovementData>();

    private MovingStructure structure;
    private boolean shouldTick = true;

    private Set<ScheduledTick> scheduledTicks = new HashSet<ScheduledTick>();

    public MovingBlock(World world, BlockPos position) {

        this.world = world;
        this.position = position;
    }

    public MovingBlock(World world, BlockPos location, Block block, int metadata, TileEntity tileentity) {

        this.world = world;
        this.position = location;
        this.block = block;
        this.metadata = metadata;
        this.tileentity = tileentity;
    }

    private boolean dwr = false;

    @Override
    public World getWorld() {

        if (!dwr) {
            dwr = true;
            World w = MovementManager.instance.getDynamicWorldReference(this);
            dwr = false;
            return w;
        }
        return world;
    }

    @Override
    public World getFakeWorld() {

        return structure != null ? FakeWorld.getFakeWorld(structure) : FakeWorld.getFakeWorld(getWorld());
    }

    @Override
    public BlockPos getPosition() {

        return position;
    }

    @Override
    public Block getBlock() {

        return block;
    }

    @Override
    public int getMetadata() {

        return metadata;
    }

    @Override
    public TileEntity getTileEntity() {

        return tileentity;
    }

    @Override
    public IExtraMovementData getExtraMovementData(String type) {

        return extraData.containsKey(type) ? extraData.get(type) : null;
    }

    @Override
    public IMovingBlock setExtraMovementData(String type, IExtraMovementData data) {

        extraData.put(type, data);
        return this;
    }

    public Map<String, IExtraMovementData> getAllExtraData() {

        return extraData;
    }

    @Override
    public MovingBlock setBlock(Block block) {

        this.block = block;
        return this;
    }

    @Override
    public MovingBlock setMetadata(int meta) {

        this.metadata = meta;
        return this;
    }

    @Override
    public MovingBlock setTileEntity(TileEntity tile) {

        this.tileentity = tile;
        return this;
    }

    @Override
    public MovingBlock snapshot() {

        block = getWorld().getBlock(getPosition().x, getPosition().y, getPosition().z);
        metadata = getWorld().getBlockMetadata(getPosition().x, getPosition().y, getPosition().z);
        tileentity = getWorld().getTileEntity(getPosition().x, getPosition().y, getPosition().z);

        return this;
    }

    @Override
    public MovingStructure getStructure() {

        return structure;
    }

    public MovingBlock setStructure(MovingStructure structure) {

        this.structure = structure;
        return this;
    }

    public MovingBlock setShouldTick(boolean shouldTick) {

        this.shouldTick = shouldTick;
        return this;
    }

    public void tick() {

        for (ScheduledTick tick : new HashSet<ScheduledTick>(scheduledTicks)) {
            if (tick.tick()) {
                scheduledTicks.remove(tick);
                block.updateTick(getFakeWorld(), position.x, position.y, position.z, getWorld().rand);
            }
        }

        if (shouldTick && getTileEntity() != null)
            getTileEntity().updateEntity();
    }

    public void startMovingPre() {

        if (!getWorld().isRemote)
            snapshot();
    }

    public void startMoving(boolean invalidate, boolean validate) {

        TileEntity te = getTileEntity();

        if (te != null && invalidate && !getWorld().isRemote)
            te.invalidate();

        BlockUtils.sneakySetBlock(getWorld(), getPosition().x, getPosition().y, getPosition().z, Blocks.air, 0);

        if (te != null) {
            BlockUtils.sneakyRemoveTile(getWorld(), getPosition().x, getPosition().y, getPosition().z);
            te.setWorldObj(FakeWorld.getFakeWorld(this));

            if (validate && !getWorld().isRemote)
                te.validate();
        }
    }

    public void startMovingPost() {

        doNotificationStuff(getPosition());
    }

    public void finishMovingPre() {

    }

    public void finishMoving(boolean invalidate, boolean validate) {

        BlockPos finalPosition = getStructure().getTrajectory().transformPos(getPosition());
        TileEntity te = getTileEntity();

        if (te != null && invalidate && !getWorld().isRemote)
            te.invalidate();

        BlockUtils.sneakySetBlock(getWorld(), finalPosition.x, finalPosition.y, finalPosition.z, getBlock(), getMetadata());

        if (te != null) {
            te.setWorldObj(getWorld());
            te.xCoord = finalPosition.x;
            te.yCoord = finalPosition.y;
            te.zCoord = finalPosition.z;
            te.blockType = getBlock();
            te.blockMetadata = getMetadata();

            if (validate && !getWorld().isRemote)
                te.validate();
            BlockUtils.sneakySetTile(getWorld(), finalPosition.x, finalPosition.y, finalPosition.z, te);
        }
    }

    public void finishMovingPost() {

        doNotificationStuff(getStructure().getTrajectory().transformPos(getPosition()));
    }

    public void doNotificationStuff(BlockPos pos) {

        getWorld().func_147451_t(pos.x, pos.y, pos.z);

        if (getWorld().isRemote)
            return;

        getWorld().markBlockForUpdate(pos.x, pos.y, pos.z);
        getWorld().notifyBlockChange(pos.x, pos.y, pos.z, getBlock());
        getWorld().notifyBlockOfNeighborChange(pos.x, pos.y, pos.z, getBlock());
        if (getBlock().hasComparatorInputOverride())
            getWorld().func_147453_f(pos.x, pos.y, pos.z, getBlock());
    }

    @Override
    public String toString() {

        return "MovingBlock(position=" + position + ", block=" + block + ", metadata=" + metadata + ", tileentity=" + tileentity + ")";
    }

    @Override
    public int hashCode() {

        return getPosition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof IMovingBlock ? getPosition().equals(((IMovingBlock) obj).getPosition())
                : (obj instanceof BlockPos ? getPosition().equals(obj) : false);
    }

    public void scheduleTick(int priority, long time) {

        scheduledTicks.add(new ScheduledTick(priority, time));
    }

    public Set<ScheduledTick> getScheduledTicks() {

        return scheduledTicks;
    }

    public static class ScheduledTick {

        private final int priority;
        private long time;

        public ScheduledTick(int priority, long time) {

            this.priority = priority;
            this.time = time;
        }

        public int getPriority() {

            return priority;
        }

        public long getTime() {

            return time;
        }

        public boolean tick() {

            time--;
            return time <= 0;
        }

    }

}
