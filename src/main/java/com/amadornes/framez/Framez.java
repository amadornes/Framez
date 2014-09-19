package com.amadornes.framez;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatibilityUtils;
import com.amadornes.framez.config.ConfigurationHandler;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.init.OredictHelper;
import com.amadornes.framez.init.Recipes;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.connected.ModifierProviderConnected;
import com.amadornes.framez.modifier.glass.ModifierProviderGlass;
import com.amadornes.framez.modifier.glass.clear.ModifierProviderGlassClear;
import com.amadornes.framez.modifier.iron.ModifierProviderIron;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.part.RegisterParts;
import com.amadornes.framez.proxy.CommonProxy;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:ForgeMultipart", guiFactory = ModInfo.GUI_FACTORY)
public class Framez {

    @Instance(ModInfo.MODID)
    public static Framez inst;

    @SidedProxy(serverSide = "com.amadornes.framez.proxy.CommonProxy", clientSide = "com.amadornes.framez.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static Logger log;

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {

        ConfigurationHandler.init(ev.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

        log = ev.getModLog();

        FramezApi.setup(new FramezApiImpl());

        ModifierRegistry.INST.registerModifierProvider(new ModifierProviderConnected());
        ModifierRegistry.INST.registerModifierProvider(new ModifierProviderIron());
        ModifierRegistry.INST.registerModifierProvider(new ModifierProviderGlass());
        ModifierRegistry.INST.registerModifierProvider(new ModifierProviderGlassClear());

        CompatibilityUtils.preInit(ev);
    }

    @EventHandler
    public void init(FMLInitializationEvent ev) {

        proxy.init();

        RegisterParts.init();
        FramezItems.init();
        FramezBlocks.init();
        CompatibilityUtils.registerBlocksAndItems();

        OredictHelper.init();

        CompatibilityUtils.init(ev);

        StructureTickHandler structureTickHandler = StructureTickHandler.INST;
        MinecraftForge.EVENT_BUS.register(structureTickHandler);
        FMLCommonHandler.instance().bus().register(structureTickHandler);

        com.amadornes.framez.event.EventHandler eventHandler = new com.amadornes.framez.event.EventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);

        NetworkHandler.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent ev) {

        CompatibilityUtils.postInit(ev);

        proxy.registerRenders();

        Recipes.init();

    }

}
