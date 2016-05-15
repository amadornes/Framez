package com.amadornes.trajectory.compat.fmp.ae2;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleFMP_AE2 extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new FMP_AE2MovementPlugin());
    }
}
