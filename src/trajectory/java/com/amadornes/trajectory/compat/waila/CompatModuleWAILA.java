package com.amadornes.trajectory.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.compat.CompatModule;

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

        if (!shouldRun())
            return;

        WailaProviderMoving handler = WailaProviderMoving.INST;
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }

    public static void callbackRegister(IWailaRegistrar registrar) {

        if (!shouldRun())
            return;

        WailaProviderMoving provider = WailaProviderMoving.INST;
        registrar.registerHeadProvider(provider, TileMoving.class);
        registrar.registerBodyProvider(provider, TileMoving.class);
        registrar.registerTailProvider(provider, TileMoving.class);
        registrar.registerStackProvider(provider, TileMoving.class);
    }

}
