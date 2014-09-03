package com.amadornes.framez.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleWaila extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".callbackRegister");
    }

    @Override
    public void init(FMLInitializationEvent ev) {

        WailaProviderMoving handler = WailaProviderMoving.INST;
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerRenders() {

    }

    public static void callbackRegister(IWailaRegistrar registrar) {

        WailaProviderMoving provider = WailaProviderMoving.INST;
        registrar.registerHeadProvider(provider, TileMoving.class);
        registrar.registerBodyProvider(provider, TileMoving.class);
        registrar.registerTailProvider(provider, TileMoving.class);
        registrar.registerStackProvider(provider, TileMoving.class);
    }

}
