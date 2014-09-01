package com.amadornes.framez.compat.pc;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import pneumaticCraft.api.tileentity.AirHandlerSupplier;
import pneumaticCraft.api.tileentity.IAirHandler;
import pneumaticCraft.api.tileentity.IPneumaticMachine;

import com.amadornes.framez.tile.TileMotor;

public class TileMotorPC extends TileMotor implements IPneumaticMachine {

    private double stored = 0;
    private double maxStored = 10000;

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

        return new AbstractMap.SimpleEntry((double) handler.getMaxPressure(), (double) handler.getPressure(ForgeDirection.UNKNOWN));
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        tag.setDouble("energy", stored);

        handler.writeToNBTI(tag);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);
        stored = tag.getDouble("energy");

        handler.readFromNBTI(tag);
    }

    private IAirHandler handler;

    @Override
    public IAirHandler getAirHandler() {

        initializeAirHandlerIfNeeded();
        return handler;
    }

    @Override
    public boolean isConnectedTo(ForgeDirection dir) {

        return true;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        initializeAirHandlerIfNeeded();
        handler.updateEntityI();

        sendUpdatePacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        initializeAirHandlerIfNeeded();
        handler.writeToNBTI(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        initializeAirHandlerIfNeeded();
        handler.readFromNBTI(tag);
    }

    @Override
    public void validate() {

        super.validate();

        initializeAirHandlerIfNeeded();
        handler.validateI(this);
    }

    private void initializeAirHandlerIfNeeded() {

        if (handler == null)
            handler = AirHandlerSupplier.getAirHandler(20, 25, 20000);
    }

}
