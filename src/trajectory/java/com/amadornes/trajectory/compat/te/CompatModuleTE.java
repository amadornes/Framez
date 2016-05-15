package com.amadornes.trajectory.compat.te;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleTE extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new TEMovementPlugin());
    }

}
