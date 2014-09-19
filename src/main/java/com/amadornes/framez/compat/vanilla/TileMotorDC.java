package com.amadornes.framez.compat.vanilla;

import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.PowerHelper.PowerUnit;

public class TileMotorDC extends TileMotor {

    @Override
    public boolean canMove(double power) {

        return isBeingPowered();
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public PowerUnit getPowerUnit() {

        return null;
    }

    @Override
    public void consumePower(double power) {

    }

}
