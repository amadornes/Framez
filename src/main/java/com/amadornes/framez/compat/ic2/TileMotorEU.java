package com.amadornes.framez.compat.ic2;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;

import java.util.AbstractMap;
import java.util.Map.Entry;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.tile.TileMotor;

public class TileMotorEU extends TileMotor implements IEnergySink {

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return stored >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        return new AbstractMap.SimpleEntry(maxStored, stored);
    }

    private int tick = 0;

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (tick == 0 && !worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        tick++;
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();

        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public void invalidate() {

        super.invalidate();

        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {

        return direction != getFace();
    }

    @Override
    public double getDemandedEnergy() {

        return (maxStored - stored) * Config.PowerRatios.eu;
    }

    @Override
    public int getSinkTier() {

        return 3;
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {

        double injected = Math.min(getDemandedEnergy(), amount);
        stored += injected / Config.PowerRatios.eu;
        sendUpdatePacket();
        return amount - injected;
    }

}
