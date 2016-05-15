package com.amadornes.trajectory.compat.bc;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleBC extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new BCMovementPlugin());
    }
}
