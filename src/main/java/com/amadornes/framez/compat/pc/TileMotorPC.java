package com.amadornes.framez.compat.pc;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.tile.TileMotor;

public class TileMotorPC extends TileMotor implements IPneumaticMachine {

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return handler.getPressure(ForgeDirection.UNKNOWN) >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public void consumeFramezPower(double power) {

        getAirHandler().addAir(-(int) (power * Config.PowerRatios.pcPressure), ForgeDirection.UNKNOWN);
        stored = (getAirHandler().getCurrentAir(ForgeDirection.UNKNOWN) / (double) getAirHandler().getMaxPressure()) * maxStored;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        return new AbstractMap.SimpleEntry((double) getAirHandler().getMaxPressure(), (double) getAirHandler().getPressure(
                ForgeDirection.UNKNOWN));
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        getAirHandler().writeToNBTI(tag);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        getAirHandler().readFromNBTI(tag);
    }

    private IAirHandler handler;

    @Override
    public IAirHandler getAirHandler() {

        initializeAirHandlerIfNeeded();
        return handler;
    }

    @Override
    public boolean isConnectedTo(ForgeDirection dir) {

        return getFace() != dir;
    }

    private double oldPower;

    @Override
    public void updateEntity() {

        super.updateEntity();

        getAirHandler().updateEntityI();

        oldPower = stored;
        stored = (getAirHandler().getCurrentAir(ForgeDirection.UNKNOWN) / (double) getAirHandler().getMaxPressure()) * maxStored;
        if (stored != oldPower)
            sendUpdatePacket();
    }

    @Override
    public void validate() {

        super.validate();

        getAirHandler().validateI(this);
    }

    private void initializeAirHandlerIfNeeded() {

        if (handler == null) {
            handler = AirHandlerSupplier.getAirHandler(20, 25, 20000);
        }
    }

}
