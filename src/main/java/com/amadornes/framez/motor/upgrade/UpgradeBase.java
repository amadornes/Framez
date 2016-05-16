package com.amadornes.framez.motor.upgrade;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorTrigger;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorVariable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public class UpgradeBase implements IMotorUpgrade {

    public final DynamicReference<? extends IMotor> motor;
    protected final int slot;
    private ResourceLocation type;

    public UpgradeBase(DynamicReference<? extends IMotor> motor, int slot) {

        this.motor = motor;
        this.slot = slot;
    }

    public UpgradeBase setType(ResourceLocation type) {

        this.type = type;
        return this;
    }

    @Override
    public ResourceLocation getType() {

        return type;
    }

    @Override
    public Map<IMotorVariable<?>, Object> getProvidedVariables() {

        return Collections.emptyMap();
    }

    @Override
    public Collection<IMotorTrigger> getProvidedTriggers() {

        return Collections.emptyList();
    }

    @Override
    public boolean hasConfigGUI() {

        return false;
    }

    @Override
    public Container getGuiContainer(EntityPlayer player) {

        return null;
    }

    @Override
    public GuiScreen getConfigGUI(EntityPlayer player, GuiScreen parent) {

        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side) {

        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side) {

        return null;
    }

    @Override
    public <T> T alterValue(T value, IMotorVariable<T> variable) {

        return value;
    }

    @Override
    public int getAlterationPriority(IMotorVariable<?> variable) {

        return 0;
    }

    @Override
    public NBTTagCompound serializeNBT() {

        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

}
