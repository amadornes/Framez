package com.amadornes.framez.compat;

import com.amadornes.framez.compat.ModularRegistry.Dependency;
import com.amadornes.framez.compat.charset.wires.CompatModuleCharsetWires;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfoCompat.MODID, name = ModInfoCompat.NAME, version = ModInfoCompat.VERSION)
public class FramezCompat {

    @Instance
    public static FramezCompat instance;

    private static final ModularRegistry<IModule> modules = new ModularRegistry<IModule>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        modules.register("CharsetWires", Dependency.MOD.on("CharsetWires"), CompatModuleCharsetWires.class);

        modules.compileRegistry();

        modules.forEach(m -> m.preInit(event));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        modules.forEach(m -> m.init(event));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        modules.forEach(m -> m.postInit(event));
    }

}
