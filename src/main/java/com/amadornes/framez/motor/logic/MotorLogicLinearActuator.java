package com.amadornes.framez.motor.logic;

import java.util.EnumSet;
import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.movement.Structure;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.IBlockAccess;

public class MotorLogicLinearActuator implements IMotorLogic {

    private DynamicReference<TileMotor> motor;
    private EnumFacing face = EnumFacing.DOWN;

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

        Structure structure = Structure.discover(new IBlockAccess[] { motor.get().getMotorWorld() }, motor.get().getMotorPos(),
                (w, p) -> w.getBlockState(p).getBlock().isAir(w, p), EnumSet.of(getFace()), false);

        return false;
    }

    @Override
    public DynamicReference<Boolean> move(Set<BlockPos> blocks) {

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
