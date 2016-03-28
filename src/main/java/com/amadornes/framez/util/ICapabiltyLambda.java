package com.amadornes.framez.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface ICapabiltyLambda extends ICapabilityProvider {

    public Object getCap(Capability<?> capability, EnumFacing facing);

    @SuppressWarnings("unchecked")
    @Override
    default <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        return (T) getCap(capability, facing);
    }

    @Override
    default boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        return getCapability(capability, facing) != null;
    }

}
