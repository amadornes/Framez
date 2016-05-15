package com.amadornes.framez.compat.bc;

import buildcraft.api.statements.IActionExternal;

import com.amadornes.framez.tile.TileMotor;

public class ActionMove extends ActionFramez {

    public static final IActionExternal INST = new ActionMove();

    public ActionMove() {

        super("move", "Attempts to move the blocks attached to the motor.");
    }

    @Override
    public void activate(TileMotor motor) {

        motor.move(false);
    }

}
