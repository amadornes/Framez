package com.amadornes.framez.tile;

import com.amadornes.framez.movement.MovementSlide;

public class TileMotorLinearActuator extends TileMotor {

    private MovementSlide movement;

    @Override
    public MovementSlide getMovement() {

        int face = getFace();
        if (movement == null || movement.getDirection() != face)
            return movement = new MovementSlide(face, 1);
        return movement;
    }

}
