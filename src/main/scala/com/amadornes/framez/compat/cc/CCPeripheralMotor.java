package com.amadornes.framez.compat.cc;

import com.amadornes.framez.tile.TileMotor;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class CCPeripheralMotor implements IPeripheral {

    private TileMotor te;

    public CCPeripheralMotor(TileMotor te) {

        this.te = te;
    }

    @Override
    public String getType() {

        return "framez_motor";
    }

    @Override
    public String[] getMethodNames() {

        return new String[] { "move", "canMove" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException,
            InterruptedException {

        if (method == 0) {
            return new Object[] { te.move(false) };
        } else if (method == 1) {
            return new Object[] { te.move(true) };
        }

        return null;
    }

    @Override
    public void attach(IComputerAccess computer) {

    }

    @Override
    public void detach(IComputerAccess computer) {

    }

    @Override
    public boolean equals(IPeripheral other) {

        return other == this;
    }

}
