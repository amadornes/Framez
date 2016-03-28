package com.amadornes.framez.motor;

import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.movement.BlockTranslation;
import com.amadornes.framez.movement.IMovementSystem;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;

public class MotorLogicLinearActuator implements IMotorLogic {

    private DynamicReference<TileMotor> motor;
    private EnumFacing face = EnumFacing.DOWN;

    @Override
    public EnumFacing getFace() {

        return face;
    }

    @Override
    public void setMotor(DynamicReference<TileMotor> motor) {

        this.motor = motor;
    }

    @Override
    public boolean rotate(EnumFacing axis) {

        EnumFacing oldFace = face;
        for (int i = 0; i < (axis.getAxisDirection() == AxisDirection.POSITIVE ? 1 : 3); i++)
            face = face.rotateAround(axis.getAxis());
        return face != oldFace;
    }

    @Override
    public double getConsumedEnergy(Set<BlockPos> blocks, double energyApplied) {

        return 0; // TODO: Determine consumed energy
    }

    @Override
    public boolean canMove(Set<BlockPos> blocks) {

        return IMovementSystem.SYSTEM.canMove(motor.get().getMotorWorld(), blocks,
                new BlockTranslation(new BlockPos(0, 0, 0).offset(face)));
    }

    @Override
    public DynamicReference<Boolean> move(Set<BlockPos> blocks) {

        IMovementSystem.SYSTEM.startMoving(motor.get().getMotorWorld(), blocks, 0,
                new BlockTranslation(new BlockPos(0, 0, 0).offset(face)));
        return new DynamicReference<Boolean>(true);
    }

    @Override
    public NBTTagCompound serializeNBT() {

        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

}
