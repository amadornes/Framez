package com.amadornes.trajectory.compat.ae2;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleAE2 extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new AE2MovementPlugin());
    }
}
