package com.amadornes.trajectory.compat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TrajectoryCompatModInfo.MODID, name = TrajectoryCompatModInfo.NAME, version = TrajectoryCompatModInfo.VERSION, dependencies = "required-after:"
        + TrajectoryCompatModInfo.TRAJECTORY_MODID)
public class TrajectoryCompat {

    @Instance(TrajectoryCompatModInfo.MODID)
    public static TrajectoryCompat instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        CompatibilityUtils.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        CompatibilityUtils.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CompatibilityUtils.postInit(event);
    }

}
