package com.amadornes.framez.compat.bc;

import buildcraft.api.statements.ITriggerExternal;

import com.amadornes.framez.tile.TileMotor;

public class TriggerCanMove extends TriggerFramez {

    public static final ITriggerExternal INST = new TriggerCanMove();

    public TriggerCanMove() {

        super("canmove", "Can motor move");
    }

    @Override
    public boolean isActive(TileMotor motor) {

        return motor.canMove();
    }

}
