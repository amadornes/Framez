package com.amadornes.framez.compat.oc;

import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

public final class EnvironmentMotor extends ManagedTileEntityEnvironment<TileMotor> implements NamedBlock {

    public EnvironmentMotor(TileMotor tileEntity) {

        super(tileEntity, ModInfo.MODID + "motor");
    }

    @Override
    public String preferredName() {

        return ModInfo.MODID + "motor";
    }

    @Override
    public int priority() {

        return 0;
    }

    @Callback(doc = "function(face):boolean -- Sets the motor's face. Returns true if it succeeded, false if it didn't.")
    public Object[] setFace(Context context, Arguments args) {

        if (args.count() == 0)
            throw new RuntimeException("At least 1 argument is required (direction)");
        return new Object[] { te.setFace(toFD(args.checkAny(0))) };
    }

    @Callback(doc = "function():number -- Gets the face the blocks that will be moved are on.")
    public Object[] getFace(Context context, Arguments args) {

        return new Object[] { te.getFace().ordinal() };
    }

    @Callback(doc = "function(direction):boolean -- Sets the motor's direction. Returns true if it succeeded, false if it didn't.")
    public Object[] setDirection(Context context, Arguments args) {

        if (args.count() == 0)
            throw new RuntimeException("At least 1 argument is required (direction)");
        return new Object[] { /* te.setDirection(toFD(args.checkAny(0))) */};
    }

    @Callback(doc = "function():number -- Gets the direction the blocks will be moved in.")
    public Object[] getDirection(Context context, Arguments args) {

        return new Object[] { /* te.getDirection().ordinal() */};
    }

    @Callback(doc = "function(face, direction):boolean -- Sets the motor's face and direction. Returns true if it succeeded, false if it didn't.")
    public Object[] set(Context context, Arguments args) {

        if (args.count() < 2)
            throw new RuntimeException("At least 2 arguments are required (face, direction)");

        ForgeDirection face = toFD(args.checkAny(0));
        ForgeDirection direction = toFD(args.checkAny(1));

        if (face == null || direction == null)
            throw new RuntimeException("Invalid directions!");
        if (face == direction || face == direction.getOpposite())
            throw new RuntimeException("Motors cannot push or pull blocks!");

        // te.setFace(face, true);
        // te.setDirection(direction, true);

        return new Object[] { true };
    }

    @Callback(doc = "function():boolean -- Attempts to move the motor. Returns true if it succeeded, false if it didn't.")
    public Object[] move(Context context, Arguments args) {

        return new Object[] { te.move() };
    }

    private ForgeDirection toFD(Object o) {

        if (o instanceof byte[])
            return ForgeDirection.valueOf(new String((byte[]) o).toUpperCase());
        else if (o instanceof Double)
            return ForgeDirection.getOrientation((Integer) o);
        return null;
    }
}