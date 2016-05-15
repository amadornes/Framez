package com.amadornes.framez.compat.bc;

import buildcraft.api.statements.ITriggerExternal;

import com.amadornes.framez.tile.TileMotor;

public class TriggerIsMoving extends TriggerFramez {

    public static final ITriggerExternal INST = new TriggerIsMoving();

    public TriggerIsMoving() {

        super("ismoving", "Is motor moving");
    }

    @Override
    public boolean isActive(TileMotor motor) {

        return motor.isMoving();
    }

}