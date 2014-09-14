package com.amadornes.framez.compat.rf;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

import com.amadornes.framez.tile.TileMotor;

public class TileMotorRF extends TileMotor implements IEnergyHandler {

    private EnergyStorage buffer = new EnergyStorage(100000, 500);

    @Override
    public boolean canMove() {

        return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        return new AbstractMap.SimpleEntry((double) getMaxEnergyStored(null) + 1, (double) getEnergyStored(null));
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {

        return from != getFace();
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

        int rec = buffer.receiveEnergy(maxReceive, simulate);

        sendUpdatePacket();

        return rec;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {

        return buffer.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return buffer.getMaxEnergyStored();
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);
        buffer.writeToNBT(tag);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);
        buffer.readFromNBT(tag);
    }

}
