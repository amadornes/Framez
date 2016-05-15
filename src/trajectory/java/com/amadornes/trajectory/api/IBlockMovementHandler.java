package com.amadornes.trajectory.api;

/**
 * Interface used to handle block removal/placement when moving.
 */
public interface IBlockMovementHandler {

    /**
     * Checks whether or not this class can handle the movement of a specific block, with a specific trajectory.
     */
    public boolean canHandle(IMovingBlock block, ITrajectory trajectory);

    /**
     * Checks whether or not the block can be moved.
     */
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory);

    /**
     * Starts moving the block. For default handling (with some customisation options) call
     * {@link IMovementManager#defaultStartMoving(IMovingBlock, boolean, boolean, boolean)}.
     */
    public void startMoving(IMovingBlock block, ITrajectory trajectory);

    /**
     * Finishes moving the block. For default handling (with some customisation options) call
     * {@link IMovementManager#defaultFinishMoving(IMovingBlock, boolean, boolean)}.
     */
    public void finishMoving(IMovingBlock block, ITrajectory trajectory);

}
