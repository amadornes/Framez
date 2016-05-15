package com.amadornes.trajectory.api;

import java.util.Set;

import net.minecraft.world.World;

/**
 * Interface that represents a moving structure.
 */
public interface IMovingStructure {

    /**
     * Gets the world this structure belongs in.
     */
    public World getWorld();

    /**
     * Gets (and loads) the fake world this structure is in while moving.
     */
    public World getFakeWorld();

    /**
     * Gets the set of blocks that form this structure.
     */
    public BlockSet getBlocks();

    /**
     * Gets the {@link ITrajectory} along which this structure moves.
     */
    public ITrajectory getTrajectory();

    /**
     * Gets the amount of ticks this structure has been moving for.
     */
    public int getTicksMoved();

    /**
     * Finishes moving the structure.
     */
    public void finishMoving();

    /**
     * Makes this structure re-render.
     */
    public void scheduleReRender();

    /**
     * Gets the callback of this structure.
     */
    public IMovementCallback getCallback();

    /**
     * Gets the set of features that this movement supports.
     */
    public Set<TrajectoryFeature> getFeatures();

}
