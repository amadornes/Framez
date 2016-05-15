package com.amadornes.framez.compat;

import java.io.File;

import com.amadornes.framez.CompatRegistryImpl;
import com.amadornes.framez.compat.upgrade.ComputerizedUpgrade;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FramezCompatModInfo.MODID, name = FramezCompatModInfo.NAME, dependencies = "required-after:framez;after:" + Dependencies.EE3)
public class FramezCompat {

    @Instance(FramezCompatModInfo.MODID)
    public static FramezCompat instance;

    public void prePreInit(File folder) {

        FramezCompatConfig.load(new File(folder, "FramezCompat.cfg"));
        CompatibilityUtils.prePreInit();
        CompatRegistryImpl.instance = new CompatRegistryAdvImpl();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        CompatibilityUtils.preInit(event);

        ModifierRegistry.instance.registerMotorCombination("eu");
        ModifierRegistry.instance.registerMotorCombination("rf");
        ModifierRegistry.instance.registerMotorCombination("eu", "rf");
        ModifierRegistry.instance.registerMotorCombination("mana");
        ModifierRegistry.instance.registerMotorCombination("pc");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        CompatibilityUtils.init(event);

        ModifierRegistry.instance.registerMotorUpgrade(ComputerizedUpgrade.upgrade);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CompatibilityUtils.postInit(event);
    }

}
