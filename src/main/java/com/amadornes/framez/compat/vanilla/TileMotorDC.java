package com.amadornes.framez.compat.vanilla;

import com.amadornes.framez.tile.TileMotor;

public class TileMotorDC extends TileMotor {

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return true;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public void consumeFramezPower(double power) {

    }

}
