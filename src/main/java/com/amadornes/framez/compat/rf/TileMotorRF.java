package com.amadornes.framez.compat.rf;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.tile.TileMotor;

public class TileMotorRF extends TileMotor implements IEnergyHandler {

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return getEnergyStored(null) >= power;
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

        double rec = Math.min(maxReceive / Config.PowerRatios.rf, maxStored - stored);

        if (!simulate)
            stored += rec;

        sendUpdatePacket();

        return (int) Math.ceil(rec * Config.PowerRatios.rf);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {

        return (int) (stored * Config.PowerRatios.rf);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {

        return (int) (maxStored * Config.PowerRatios.rf);
    }

}
