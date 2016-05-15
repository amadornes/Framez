package com.amadornes.trajectory.api;

/**
 * Interface used as a callback for moving structures.
 */
public interface IMovementCallback {

    /**
     * Called when a structure using this callback starts moving.
     */
    public void onStartMoving(IMovingStructure structure);

    /**
     * Called when a structure using this callback finishes moving.
     */
    public void onFinishMoving(IMovingStructure structure);

}
