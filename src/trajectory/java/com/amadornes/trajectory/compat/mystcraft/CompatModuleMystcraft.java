package com.amadornes.trajectory.compat.mystcraft;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleMystcraft extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new MystcraftMovementPlugin());
    }

}
