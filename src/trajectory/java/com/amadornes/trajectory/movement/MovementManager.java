package com.amadornes.trajectory.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IBlockDescriptionProvider;
import com.amadornes.trajectory.api.IBlockMovementHandler;
import com.amadornes.trajectory.api.IDynamicWorldReference;
import com.amadornes.trajectory.api.IExtraMovementData;
import com.amadornes.trajectory.api.IMovementCallback;
import com.amadornes.trajectory.api.IMovementManager;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.IMovingStructure;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryFeature;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.config.ConfigHandler;
import com.google.gson.JsonElement;

public class MovementManager implements IMovementManager {

    public static MovementManager instance = new MovementManager();

    private List<IBlockMovementHandler> movementHandlers = new ArrayList<IBlockMovementHandler>();
    private List<IBlockDescriptionProvider> dataProviders = new ArrayList<IBlockDescriptionProvider>();
    private Map<String, Class<? extends ITrajectory>> trajectories = new HashMap<String, Class<? extends ITrajectory>>();
    private List<IDynamicWorldReference> dynamicWorldReferences = new ArrayList<IDynamicWorldReference>();

    private List<MovingStructure> structuresC = new ArrayList<MovingStructure>(), structuresS = new ArrayList<MovingStructure>();

    @Override
    public boolean canMove(BlockSet blocks, ITrajectory trajectory) {

        if (ConfigHandler.block_cap > 0 && blocks.size() > ConfigHandler.block_cap)
            return false;

        for (IMovingBlock bl : blocks) {
            BlockPos t = trajectory.transformPos(bl.getPosition());
            if (!blocks.getWorld().blockExists(t.x, t.y, t.z) || t.y < 0 || t.y >= blocks.getWorld().getActualHeight()
                    || isMoving(blocks.getWorld(), bl.getPosition()) || isMoving(blocks.getWorld(), t) || !canBeMoved(bl, trajectory))
                return false;
        }

        return true;
    }

    @Override
    public boolean isMoving(World world, BlockPos position) {

        if (world.getTileEntity(position.x, position.y, position.z) instanceof TileMoving)
            return true;

        for (MovingStructure s : getStructures(world.isRemote)) {
            if (s.getWorld() == world && s.getTrajectory().getProgress(s.getTicksMoved()) < 1) {
                Set<TileMoving> placeholders = s.getPlaceholders();
                if (placeholders != null) {
                    for (TileMoving te : placeholders)
                        if (te.xCoord == position.x && te.yCoord == position.y && te.zCoord == position.z)
                            return true;
                } else {
                    for (Entry<BlockPos, Boolean> e : s.getTrajectory().getPlaceholderPositions(s.getBlocks()).entrySet()) {
                        if (!e.getValue())
                            continue;
                        if (e.getKey().equals(position))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory) {

        for (IBlockMovementHandler handler : movementHandlers)
            if (handler.canHandle(block, trajectory))
                return handler.canBeMoved(block, trajectory);

        return true;
    }

    @Override
    public IMovingStructure startMoving(BlockSet blocks, ITrajectory trajectory, Set<TrajectoryFeature> features) {

        return startMoving(blocks, trajectory, features, null);
    }

    @Override
    public IMovingStructure startMoving(BlockSet blocks, ITrajectory trajectory, Set<TrajectoryFeature> features, IMovementCallback callback) {

        return startMoving(new MovingStructure(UUID.randomUUID(), blocks, trajectory, features, callback));
    }

    public MovingStructure startMoving(MovingStructure structure) {

        getStructures(structure.getWorld().isRemote).add(structure);
        return structure;
    }

    @Override
    public void registerBlockMovementHandler(IBlockMovementHandler handler) {

        if (handler == null)
            throw new NullPointerException("A mod attempted to register a null movement handler! Report this to the author!");
        if (movementHandlers.contains(handler))
            throw new IllegalStateException(
                    "A mod attempted to register a movement handler that was already registered! Report this to the author!");

        movementHandlers.add(handler);

    }

    @Override
    public void registerBlockDescriptionProvider(IBlockDescriptionProvider provider) {

        if (provider == null)
            throw new NullPointerException("A mod attempted to register a null data provider! Report this to the author!");
        if (dataProviders.contains(provider))
            throw new IllegalStateException(
                    "A mod attempted to register a data provider that was already registered! Report this to the author!");
        for (IBlockDescriptionProvider p : dataProviders)
            if (p.getType().equals(provider.getType()))
                throw new IllegalStateException(
                        "A mod attempted to register a data provider of a type that was already registered! Report this to the author!");

        dataProviders.add(provider);
    }

    @Override
    public void registerTrajectoryType(Class<? extends ITrajectory> trajectoryClass, String identifier) {

        if (trajectoryClass == null)
            throw new NullPointerException("A mod attempted to register a null trajectory! Report this to the author!");
        if (identifier == null)
            throw new NullPointerException("A mod attempted to register a null trajectory type! Report this to the author!");
        if (trajectories.containsValue(trajectoryClass))
            throw new IllegalStateException(
                    "A mod attempted to register a trajectory that had already been registered! Report this to the author!");
        if (trajectories.containsKey(identifier))
            throw new IllegalStateException(
                    "A mod attempted to register a trajectory type that had already been registered! Report this to the author!");

        trajectories.put(identifier, trajectoryClass);
    }

    @Override
    public void registerDynamicWorldReference(IDynamicWorldReference reference) {

        if (reference == null)
            throw new NullPointerException("A mod attempted to register a null dynamic world reference! Report this to the author!");
        if (dynamicWorldReferences.contains(reference))
            throw new IllegalStateException(
                    "A mod attempted to register a dynamic world reference that had already been registered! Report this to the author!");

        dynamicWorldReferences.add(reference);
    }

    @Override
    public IMovingBlock createBlock(World world, BlockPos position) {

        return new MovingBlock(world, position).snapshot();
    }

    @Override
    public void defaultStartMoving(IMovingBlock block, boolean invalidate, boolean validate, boolean shouldTick) {

        ((MovingBlock) block).setShouldTick(shouldTick).startMoving(invalidate, validate);
    }

    @Override
    public void defaultFinishMoving(IMovingBlock block, boolean invalidate, boolean validate) {

        ((MovingBlock) block).finishMoving(invalidate, validate);
    }

    public MovingStructure findStructure(boolean isRemote, UUID id) {

        for (MovingStructure structure : getStructures(isRemote))
            if (structure.getId().equals(id))
                return structure;

        return null;
    }

    public ITrajectory recreateTrajectory(String type, NBTTagCompound tag) {

        if (!trajectories.containsKey(type))
            throw new IllegalStateException("Received information about a trajectory of type \"" + type
                    + "\", which isn't registered. Aborting client synchronization!");

        try {
            Class<? extends ITrajectory> clazz = trajectories.get(type);
            ITrajectory trajectory = clazz.newInstance();
            trajectory.readFromNBT(tag);
            return trajectory;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ITrajectory parseTrajectory(String type, JsonElement json) throws Exception {

        if (!trajectories.containsKey(type))
            throw new IllegalStateException("Attempted to load a trajectory of type \"" + type + "\", which isn't registered!");

        Class<? extends ITrajectory> clazz = trajectories.get(type);
        ITrajectory trajectory;
        try {
            trajectory = clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Attempted to instantiate a trajectory of type \"" + type + "\", but failed!");
        }
        trajectory.parseJSON(json);
        return trajectory;
    }

    public String getTrajectoryType(ITrajectory trajectory) {

        for (Entry<String, Class<? extends ITrajectory>> e : trajectories.entrySet())
            if (e.getValue().equals(trajectory.getClass()))
                return e.getKey();
        throw new IllegalStateException(
                "Attempting to get the identifier for a trajectory which is not registered! Report this to the author! "
                        + trajectory.getClass());
    }

    public NBTTagCompound describeBlock(MovingBlock block) {

        NBTTagCompound tag = new NBTTagCompound();

        for (IBlockDescriptionProvider describer : dataProviders) {
            if (describer.canHandle(block)) {
                describer.writeBlockData(block, tag);
                tag.setString("__type", describer.getType());
                break;
            }
        }

        NBTTagList extraData = new NBTTagList();
        for (Entry<String, IExtraMovementData> e : block.getAllExtraData().entrySet()) {
            NBTTagCompound t = new NBTTagCompound();
            e.getValue().writeToNBT(tag);
            t.setString("__type", e.getKey());
            t.setString("__class", e.getValue().getClass().getName());
            extraData.appendTag(t);
        }
        tag.setTag("__extradata", extraData);

        return tag;
    }

    public <T extends IMovingBlock> T readDescription(T block, NBTTagCompound tag) {

        for (IBlockDescriptionProvider describer : dataProviders) {
            if (describer.getType().equals(tag.getString("__type"))) {
                describer.readBlockData(block, tag);
                break;
            }
        }

        NBTTagList extraData = tag.getTagList("__extradata", new NBTTagCompound().getId());
        for (int i = 0; i < extraData.tagCount(); i++) {
            NBTTagCompound t = extraData.getCompoundTagAt(i);
            String type = t.getString("__type");
            try {
                Class<?> c = Class.forName(t.getString("__class"));
                IExtraMovementData data = (IExtraMovementData) c.newInstance();
                data.readFromNBT(t);
                block.setExtraMovementData(type, data);
            } catch (Exception ex) {
                throw new RuntimeException("An issue occoured while trying to read extra movement data of type \"" + type + "\"", ex);
            }
        }

        return block;
    }

    public MovingBlock readDescription(World world, BlockPos position, NBTTagCompound tag) {

        return readDescription((MovingBlock) createBlock(world, position), tag);
    }

    public List<MovingStructure> getStructures(boolean isRemote) {

        return isRemote ? structuresC : structuresS;
    }

    public World getDynamicWorldReference(IMovingBlock block) {

        for (IDynamicWorldReference ref : dynamicWorldReferences) {
            World w = ref.getWorld(block);
            if (w != null)
                return w;
        }

        return block.getWorld();
    }

    public void startMoving(MovingBlock block, ITrajectory trajectory) {

        for (IBlockMovementHandler handler : movementHandlers) {
            if (handler.canHandle(block, trajectory)) {
                handler.startMoving(block, trajectory);
                return;
            }
        }
        if (!block.getWorld().isRemote)
            defaultStartMoving(block, true, true, true);
    }

    public void finishMoving(MovingBlock block, ITrajectory trajectory) {

        for (IBlockMovementHandler handler : movementHandlers) {
            if (handler.canHandle(block, trajectory)) {
                handler.finishMoving(block, trajectory);
                block.getStructure().getTrajectory().transformBlock(block);
                return;
            }
        }
        if (!block.getWorld().isRemote)
            defaultFinishMoving(block, true, true);
    }

}
