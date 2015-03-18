package com.amadornes.framez.tile;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.movement.MovementSlide;

public class TileMotorSlider extends TileMotor {

    private IMovement movement = new MovementSlide(ForgeDirection.SOUTH);

    @Override
    public IMovement getMovement() {

        if (movement == null)
            movement = new MovementSlide(ForgeDirection.SOUTH);

        return movement;
    }

}
