package com.amadornes.trajectory.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amadornes.trajectory.compat.ae2.CompatModuleAE2;
import com.amadornes.trajectory.compat.bc.CompatModuleBC;
import com.amadornes.trajectory.compat.cc.CompatModuleCC;
import com.amadornes.trajectory.compat.fmp.CompatModuleFMP;
import com.amadornes.trajectory.compat.fmp.ae2.CompatModuleFMP_AE2;
import com.amadornes.trajectory.compat.mystcraft.CompatModuleMystcraft;
import com.amadornes.trajectory.compat.nei.CompatModuleNEI;
import com.amadornes.trajectory.compat.td.CompatModuleTD;
import com.amadornes.trajectory.compat.te.CompatModuleTE;
import com.amadornes.trajectory.compat.waila.CompatModuleWAILA;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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

    public static void registerModule(String modid, Class<? extends CompatModule> module) {

        registerModule(modid, module, null);
    }

    private static boolean hasClass(String clazz) {

        try {
            Class.forName(clazz);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    public static void registerModule(String modid, String module, String alternate) {

        if (modid == null || modid.trim().isEmpty())
            return;
        if (module == null)
            return;
        if (modules.containsKey(module))
            return;

        if ((modid.startsWith("api:") && ModAPIManager.INSTANCE.hasAPI(modid.substring(4)))
                || (modid.startsWith("class:") && hasClass(modid.substring(6))) || Loader.isModLoaded(modid)) {
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

    /**
     * Register modules here
     */
    static {
        registerModule("Waila", CompatModuleWAILA.class);
        registerModule("NotEnoughItems", CompatModuleNEI.class);
        registerModule("ComputerCraft", CompatModuleCC.class);
        registerModule("BuildCraft|Core", CompatModuleBC.class);
        registerModule("appliedenergistics2", CompatModuleAE2.class);
        registerModule("ForgeMultipart", CompatModuleFMP.class);
        registerModule("ThermalDynamics", CompatModuleTD.class);
        registerModule("Mystcraft", CompatModuleMystcraft.class);
        registerModule("ThermalExpansion", CompatModuleTE.class);

        // FMP Plugins for other mods
        if (Loader.isModLoaded("ForgeMultipart")) {
            if (Loader.isModLoaded("appliedenergistics2"))
                registerModule(UUID.randomUUID().toString(), CompatModule.class, CompatModuleFMP_AE2.class);
        }
    }

}
