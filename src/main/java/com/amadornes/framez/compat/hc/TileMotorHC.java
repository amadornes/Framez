package com.amadornes.framez.compat.hc;

import java.util.AbstractMap;
import java.util.Map.Entry;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.tile.TileMotor;

public class TileMotorHC extends TileMotor implements IHydraulicConsumer {

    @Override
    public boolean shouldMove() {

        return isBeingPowered();
    }

    @Override
    public boolean hasEnoughFramezPower(double power) {

        return c.getPressure(ForgeDirection.UP) >= power;
    }

    @Override
    public double getMovementSpeed() {

        return 1;
    }

    @Override
    public void consumeFramezPower(double power) {

        int pow = c.getStored() - (int) (power * Config.PowerRatios.hcPressure);
        c.setPressure(pow, ForgeDirection.UP);
        stored = (pow / (double) c.getMaxStorage()) * maxStored;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Entry<Double, Double> getExtraInfo() {

        return new AbstractMap.SimpleEntry(maxStored, stored);
    }

    @Override
    public boolean canConnectTo(ForgeDirection face) {

        return true;
    }

    private IBaseClass c = HydraulicBaseClassSupplier.getBaseClass(this, PressureTier.HIGHPRESSURE, 250);

    @Override
    public IBaseClass getHandler() {

        return c;
    }

    @Override
    public void onFluidLevelChanged(int lvl) {

        c.updateFluidOnNextTick();
    }

    @Override
    public boolean canWork(ForgeDirection face) {

        return false;
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection face) {

        return 0;
    }

    private double oldPower = 0;

    @Override
    public void updateEntity() {

        super.updateEntity();

        c.updateEntityI();

        oldPower = stored;
        stored = (c.getPressure(ForgeDirection.UP) / (double) c.getMaxPressure(true, ForgeDirection.UP)) * maxStored;
        if (stored != oldPower)
            sendUpdatePacket();
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        c.writeToNBTI(tag);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        c.readFromNBTI(tag);
    }

}
