package com.amadornes.framez.compat.hc;

import java.util.AbstractMap;
import java.util.Map.Entry;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.tile.TileMotor;

public class TileMotorHC extends TileMotor implements IHydraulicConsumer {

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

        return new AbstractMap.SimpleEntry((double) c.getStored(), (double) c.getMaxStorage());
    }

    @Override
    public boolean canConnectTo(ForgeDirection face) {

        return true;
    }

    private IBaseClass c = HydraulicBaseClassSupplier.getBaseClass(this, PressureTier.HIGHPRESSURE, 10);

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

        return canMove();
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection face) {

        return 0;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        c.updateEntityI();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        c.writeToNBTI(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        c.readFromNBTI(tag);
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
