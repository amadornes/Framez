package com.amadornes.framez.compat.cc;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.movement.MovementSlide;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class PeripheralMotor implements IPeripheral {

    private TileMotor te;

    public PeripheralMotor(TileMotor te) {

        this.te = te;
    }

    @Override
    public String getType() {

        return ModInfo.MODID + ".motor";
    }

    @Override
    public String[] getMethodNames() {

        return new String[] { "setFace", "getFace", "setDirection", "getDirection", "set", "move" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException,
    InterruptedException {

        if (method == 0) {
            if (arguments.length == 0)
                throw new LuaException("At least 1 argument is required (face)");
            return new Object[] { te.setFace(toFD(arguments[0])) };
        } else if (method == 1) {
            return new Object[] { te.getFace().ordinal() };
        } else if (method == 2) {
            if (!(te.getMovement() instanceof MovementSlide))
                throw new LuaException("This is not a slider motor!");
            if (arguments.length == 0)
                throw new LuaException("At least 1 argument is required (direction)");
            ((IMovementSlide) te.getMovement()).setDirection(toFD(arguments[0]));
            return new Object[] {};
        } else if (method == 3) {
            if (!(te.getMovement() instanceof MovementSlide))
                throw new LuaException("This is not a slider motor!");
            return new Object[] { ((IMovementSlide) te.getMovement()).getDirection().ordinal() };
        } else if (method == 4) {
            throw new LuaException("Not implemented yet, sorry D:");
            // if (arguments.length < 2)
            // throw new LuaException("At least 2 arguments are required (face, direction)");
            //
            // ForgeDirection face = toFD(arguments[0]);
            // ForgeDirection direction = toFD(arguments[1]);
            //
            // if (face == null || direction == null)
            // throw new LuaException("Invalid directions!");
            // if (face == direction || face == direction.getOpposite())
            // throw new LuaException("Motors cannot push or pull blocks!");
            //
            // te.setFace(face, true);
            // te.setDirection(direction, true);
            //
            // return new Object[] { true };
        } else if (method == 5) {
            return new Object[] { te.move() };
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

    private ForgeDirection toFD(Object o) {

        // if (o instanceof String)
        // return ForgeDirection.valueOf(((String) o).toUpperCase());
        // else if (o instanceof Integer)
        // return ForgeDirection.getOrientation((Integer) o);
        return ForgeDirection.UP;
    }

}
