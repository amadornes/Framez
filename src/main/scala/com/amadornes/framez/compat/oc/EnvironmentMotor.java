package com.amadornes.framez.compat.oc;

import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.integration.ManagedTileEntityEnvironment;

import com.amadornes.framez.tile.TileMotor;

public final class EnvironmentMotor extends ManagedTileEntityEnvironment<TileMotor> implements NamedBlock {

    public EnvironmentMotor(TileMotor te) {

        super(te, "framez_motor");
    }

    @Override
    public String preferredName() {

        return "framez_motor";
    }

    @Override
    public int priority() {

        return 0;
    }

    @Callback(doc = "function():boolean -- Makes the motor move")
    public Object[] move(Context context, Arguments args) {

        return new Object[] { tileEntity.move(false) };
    }

    @Callback(doc = "function():boolean -- Checks whether or not the motor can move")
    public Object[] canMove(Context context, Arguments args) {

        return new Object[] { tileEntity.move(true) };
    }
}