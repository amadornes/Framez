package com.amadornes.framez.api.movement;

public enum BlockMovementType {

    /**
     * The block WILL stick to frames
     */
    MOVABLE(true, false),
    /**
     * The block WILL stick to frames and WON'T prevent them from moving if it's in the way (they'll just delete the block)
     */
    SEMI_MOVABLE(true, true),
    /**
     * The block WON'T stick to frames and WON'T prevent them from moving if it's in the way (they'll just delete the block)
     */
    REPLACEABLE(false, true),
    /**
     * The block WON'T stick to frames and WILL prevent them from moving if it's in the way
     */
    UNMOVABLE(false, false);

    private boolean movable;
    private boolean replaceable;

    private BlockMovementType(boolean movable, boolean replaceable) {

        this.movable = movable;
        this.replaceable = replaceable;
    }

    public boolean isMovable() {

        return movable;
    }

    public boolean isReplaceable() {

        return replaceable;
    }

}
