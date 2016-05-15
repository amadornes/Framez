package com.amadornes.framez.movement;

import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.movement.TrajectoryTranslation;
import com.amadornes.trajectory.util.MiscUtils;

public class MovementSlide extends TrajectoryTranslation implements IMovementSlide {

    private int direction;

    public MovementSlide(int direction, double speed) {

        super(BlockPos.sideOffsets[direction], speed);
        this.direction = direction;
    }

    public MovementSlide() {

        super();
    }

    @Override
    public int getDirection() {

        return direction;
    }

    @Override
    public void setDirection(int dir) {

        direction = dir;
        translation = BlockPos.sideOffsets[dir];
    }

    @Override
    public MovementSlide copy() {

        return new MovementSlide(getDirection(), getSpeed());
    }

    @Override
    public IMovement withSpeed(double speed) {

        return new MovementSlide(getDirection(), speed);
    }

    @Override
    public boolean rotate(int axis, IMotor motor) {

        setDirection(MiscUtils.rotate(getDirection(), axis));
        motor.setFace(MiscUtils.rotate(motor.getFace(), axis));

        return true;
    }

    @Override
    public double getSpeedMultiplier(BlockSet blocks) {

        return 1;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("direction", direction);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        direction = tag.getInteger("direction");
    }
}
