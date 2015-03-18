package com.amadornes.framez.api.movement;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.IDebuggable;

public interface IMovement extends IDebuggable {

    public MovementType getMovementType();

    public Vec3i transform(Vec3i position);

    public Vec3d transform(Vec3d position, double progress);

    public boolean rotate(IMotor mover, ForgeDirection axis);

    public boolean clashes(ForgeDirection direction);

    public IMovement clone();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public static enum MovementType {
        SLIDE, ROTATION;
    }

    public static interface IMovementSlide extends IMovement {

        public ForgeDirection getDirection();

        public void setDirection(ForgeDirection direction);
    }

    public static interface IMovementRotation extends IMovement {

        public ForgeDirection getAxis();

        public void setAxis(ForgeDirection direction);

        public Vec3i getCenter();
    }

}
