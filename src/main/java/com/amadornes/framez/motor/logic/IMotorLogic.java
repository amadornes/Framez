package com.amadornes.framez.motor.logic;

import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMotorLogic extends INBTSerializable<NBTTagCompound> {

    @SuppressWarnings("unchecked")
    public static final Class<IMotorLogic>[] TYPES = new Class[] { MotorLogicLinearActuator.class, MotorLogicRotator.class };
    public static final String[] TYPE_NAMES = new String[] { "linear_actuator", "rotator" };
    public static final String[] TYPE_FTESRS = new String[] { "RenderLinearActuator", "RenderRotator" };

    public static IMotorLogic create(int meta) {

        try {
            return TYPES[meta].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public default int getID() {

        for (int i = 0; i < TYPES.length; i++)
            if (TYPES[i] == getClass()) return i;
        return -1;
    }

    public EnumFacing getFace();

    public TileMotor getMotor();

    public void setMotor(DynamicReference<TileMotor> motor);

    public boolean rotate(EnumFacing axis);

    public double getConsumedEnergy(Set<BlockPos> blocks, double energyApplied);

    public boolean canMove(Set<BlockPos> blocks);

    public DynamicReference<Boolean> move(Set<BlockPos> blocks);

}
