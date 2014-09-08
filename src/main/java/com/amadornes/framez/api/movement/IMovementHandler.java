package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

/**
 * This interface is used to handle the movement of a block. ALL blocks will go through this methods. Return true if the block was handled, false if
 * not. As soon as a handler returns true the rest will be skipped.
 * 
 * @author amadornes
 * 
 */
public interface IMovementHandler {

    /**
     * Handles a block's movement start. You can use the @{@link HandlingPriority} annotation to set the priority this handler has.
     * 
     * @param block
     *            Information about the block
     * @return Whether or not the block's movement was handled
     */
    public boolean handleStartMoving(IMovingBlock block);

    /**
     * Handles a block's movement end. You can use the @{@link HandlingPriority} annotation to set the priority this handler has.
     * 
     * @param block
     *            Information about the block
     * @return Whether or not the block's movement was handled
     */
    public boolean handleFinishMoving(IMovingBlock block);

    /**
     * Returns the movement type of this block or null if it wasn't handled. Once a handler returns a value the rest will be skipped and won't be
     * handled. You can use the @{@link HandlingPriority} annotation to set the priority this handler has.
     * 
     * @param w
     *            World object
     * @param x
     *            X coordinate of the block
     * @param y
     *            Y coordinate of the block
     * @param z
     *            Z coordinate of the block
     * @return The movement type (or null if it wasn't handled)
     */
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z);

}
