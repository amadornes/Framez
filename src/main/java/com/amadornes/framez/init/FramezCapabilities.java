package com.amadornes.framez.init;

import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IStickable;
import com.amadornes.framez.api.frame.ISticky;
import com.amadornes.framez.api.motor.IMotor;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class FramezCapabilities {

    public static void register() {

        CapabilityManager.INSTANCE.register(IMotor.class, new NullStorage<IMotor>(), () -> null);
        CapabilityManager.INSTANCE.register(IFrame.class, new NullStorage<IFrame>(), () -> null);
        CapabilityManager.INSTANCE.register(ISticky.class, new NullStorage<ISticky>(), () -> (ISticky) () -> false);
        CapabilityManager.INSTANCE.register(IStickable.class, new NullStorage<IStickable>(), () -> (IStickable) () -> true);
    }

    private static final class NullStorage<T> implements IStorage<T> {

        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {

            return null;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {

        }

    }

}
