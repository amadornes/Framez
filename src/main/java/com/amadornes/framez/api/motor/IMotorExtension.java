package com.amadornes.framez.api.motor;

import java.util.Collection;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMotorExtension extends INBTSerializable<NBTTagCompound> {

    public Map<? extends IMotorVariable<?>, Object> getProvidedVariables();

    public Collection<? extends IMotorTrigger> getProvidedTriggers();

    public boolean hasCapability(Capability<?> capability, EnumFacing side);

    public <T> T getCapability(Capability<T> capability, EnumFacing side);

    public <T> T alterValue(T value, IMotorVariable<T> variable);

    public int getAlterationPriority(IMotorVariable<?> variable);

}
