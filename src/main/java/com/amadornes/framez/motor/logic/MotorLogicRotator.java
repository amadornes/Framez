package com.amadornes.framez.motor.logic;

import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;

public class MotorLogicRotator implements IMotorLogic {

    private DynamicReference<TileMotor> motor;
    private EnumFacing face = EnumFacing.DOWN;
    private AxisDirection direction = AxisDirection.NEGATIVE;

    @Override
    public EnumFacing getFace() {

        return face;
    }

    @Override
    public TileMotor getMotor() {

        return motor.get();
    }

    @Override
    public void setMotor(DynamicReference<TileMotor> motor) {

        this.motor = motor;
    }

    @Override
    public boolean rotate(EnumFacing axis) {

        if (axis.getAxis() == face.getAxis()) {
            direction = direction == AxisDirection.NEGATIVE ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
            return true;
        }
        for (int i = 0; i < (axis.getAxisDirection() == AxisDirection.POSITIVE ? 1 : 3); i++)
            face = face.rotateAround(axis.getAxis());
        return true;
    }

    @Override
    public double getConsumedEnergy(Set<BlockPos> blocks, double energyApplied) {

        return 0; // TODO: Determine consumed energy
    }

    @Override
    public boolean canMove(Set<BlockPos> blocks) {

        return false;
    }

    @Override
    public DynamicReference<Boolean> move(Set<BlockPos> blocks) {

        return new DynamicReference<Boolean>(false);
    }

    @Override
    public NBTTagCompound serializeNBT() {

        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

}
