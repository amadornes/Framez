package com.amadornes.framez.tile;

import com.amadornes.framez.movement.MovementRotation;
import com.amadornes.trajectory.api.vec.BlockPos;

public class TileMotorRotator extends TileMotor {

    private MovementRotation movement;

    @Override
    public MovementRotation getMovement() {

        if (movement == null || movement.getOriginBlock().x != getX() || movement.getOriginBlock().y != getY()
                || movement.getOriginBlock().z != getZ() || (movement.getAxis() != getFace() && movement.getAxis() != (getFace() ^ 1)))
            movement = new MovementRotation(new BlockPos(getX(), getY(), getZ()), getFace(), 1);
        return movement;
    }
}
