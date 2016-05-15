package com.amadornes.framez.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleWAILA extends CompatModule {

    private static final boolean shouldRun() {

        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (!shouldRun())
            return;

        FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".callbackRegister");
    }

    @Override
    public void init(FMLInitializationEvent ev) {

    }

    public static void callbackRegister(IWailaRegistrar registrar) {

        if (!shouldRun())
            return;

        if (FramezCompatConfig.waila_motor_info) {
            WailaProviderMotor provider = new WailaProviderMotor();
            registrar.registerBodyProvider(provider, TileMotor.class);
        }
    }

}
