package com.amadornes.framez.compat.vanilla;

import com.amadornes.framez.tile.TileMotor;

public class TileMotorDC extends TileMotor {

    @Override
    public boolean canMove() {

        return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

}
