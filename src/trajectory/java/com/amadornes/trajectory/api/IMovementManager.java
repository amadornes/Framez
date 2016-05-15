package com.amadornes.trajectory.api;

import java.util.Set;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

public interface IMovementManager {

    /**
     * Checks whether or not a set of blocks can be moved.
     */
    public boolean canMove(BlockSet blocks, ITrajectory trajectory);

    /**
     * Checks whether or not the block at a specific position is moving.
     */
    public boolean isMoving(World world, BlockPos position);

    /**
     * Checks whether or not the specified block can be moved following the specified trajectory.
     */
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory);

    /**
     * Starts moving a set of blocks following a trajectory, with the specified set of movement features.
     */
    public IMovingStructure startMoving(BlockSet blocks, ITrajectory trajectory, Set<TrajectoryFeature> features);

    /**
     * Starts moving a set of blocks following a trajectory, with the specified set of movement features and a movement callback.
     */
    public IMovingStructure startMoving(BlockSet blocks, ITrajectory trajectory, Set<TrajectoryFeature> features, IMovementCallback callback);

    /**
     * Registers a new {@link IBlockMovementHandler}.
     */
    public void registerBlockMovementHandler(IBlockMovementHandler handler);

    /**
     * Registers a new {@link IBlockDescriptionProvider}.
     */
    public void registerBlockDescriptionProvider(IBlockDescriptionProvider provider);

    /**
     * Registers a new {@link ITrajectory} class. Used to sync the server and client.
     */
    public void registerTrajectoryType(Class<? extends ITrajectory> trajectoryClass, String identifier);

    /**
     * Registers a new {@link IDynamicWorldReference}. Used to proxy fake worlds when referencing the world the structure is moving inside.
     */
    public void registerDynamicWorldReference(IDynamicWorldReference reference);

    /**
     * Creates a new {@link IMovingBlock} from a world and a position.
     */
    public IMovingBlock createBlock(World world, BlockPos position);

    /**
     * Handles default block movement. Options are invalidate TE, validate TE and whether or not the TE should tick.
     */
    public void defaultStartMoving(IMovingBlock block, boolean invalidate, boolean validate, boolean shouldTick);

    /**
     * Handles default block movement. Options are invalidate TE and validate TE.
     */
    public void defaultFinishMoving(IMovingBlock block, boolean invalidate, boolean validate);
}
