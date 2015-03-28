package com.amadornes.framez.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.IFramezWrench.WrenchAction;
import com.amadornes.framez.compat.ae2.CompatModuleAE2;
import com.amadornes.framez.compat.cc.CompatModuleCC;
import com.amadornes.framez.compat.ic2.CompatModuleIC2;
import com.amadornes.framez.compat.oc.CompatModuleOC;
import com.amadornes.framez.compat.rf.CompatModuleRF;
import com.amadornes.framez.ref.Dependencies;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
 * This file was originally created by me for use in BluePower but I thought it'd be quite useful to just copy it here instead of having
 * to rewrite it all over again, as it works perfectly as it is
 */
public class CompatibilityUtils {

    private static Map<String, CompatModule> modules = new HashMap<String, CompatModule>();

    private CompatibilityUtils() {

    }

    public static void registerModule(String modid, Class<? extends CompatModule> module, Class<? extends CompatModule> alternate) {

        registerModule(modid, module.getName(), alternate == null ? null : alternate.getName());
    }

    public static void registerModule(String modid, String module, String alternate) {

        if (modid == null || modid.trim().isEmpty())
            return;
        if (module == null)
            return;
        if (modules.containsKey(module))
            return;

        if ((modid.startsWith("api:") && ModAPIManager.INSTANCE.hasAPI(modid.substring(4))) || Loader.isModLoaded(modid)) {
            try {
                Class<?> c = Class.forName(module);
                if (!CompatModule.class.isAssignableFrom(c))
                    return;
                modules.put(modid, (CompatModule) c.newInstance());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (alternate == null || alternate.trim().isEmpty())
            return;

        try {
            Class<?> c2 = Class.forName(alternate);
            if (!CompatModule.class.isAssignableFrom(c2))
                return;
            modules.put(modid, (CompatModule) c2.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<CompatModule> getLoadedModules() {

        return Collections.unmodifiableList(new ArrayList<CompatModule>(modules.values()));
    }

    public static CompatModule getModule(String modid) {

        try {
            return modules.get(modid);
        } catch (Exception ex) {
        }

        return null;
    }

    public static void preInit(FMLPreInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.preInit(ev);
    }

    public static void init(FMLInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.init(ev);
    }

    public static void postInit(FMLPostInitializationEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.postInit(ev);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {

        for (CompatModule m : getLoadedModules())
            m.registerRenders();
    }

    public static void registerBlocksAndItems() {

        for (CompatModule m : getLoadedModules()) {
            m.registerBlocks();
            m.registerItems();
        }
    }

    public static WrenchAction getWrenchAction(ItemStack stack) {

        for (CompatModule m : getLoadedModules()) {
            WrenchAction a = m.getWrenchAction(stack);
            if (a != null)
                return a;
        }

        return null;
    }

    /**
     * Register modules here
     */
    static {

        registerModule(Dependencies.CC, CompatModuleCC.class, null);
        registerModule(Dependencies.OC, CompatModuleOC.class, null);

        registerModule("api:" + Dependencies.API_RF, CompatModuleRF.class, null);
        registerModule(Dependencies.IC2, CompatModuleIC2.class, null);
        registerModule(Dependencies.AE2, CompatModuleAE2.class, null);
    }

}
