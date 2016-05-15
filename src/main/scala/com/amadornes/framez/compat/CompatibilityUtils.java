package com.amadornes.framez.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.compat.ae2.CompatModuleAE2;
import com.amadornes.framez.compat.bc.CompatModuleBC;
import com.amadornes.framez.compat.botania.CompatModuleBotania;
import com.amadornes.framez.compat.cc.CompatModuleCC;
import com.amadornes.framez.compat.cofhlib.CompatModuleCoFHLib;
import com.amadornes.framez.compat.ee3.CompatModuleEE3;
import com.amadornes.framez.compat.floco.CompatModuleFLoco;
import com.amadornes.framez.compat.fmp.CompatModuleFMP;
import com.amadornes.framez.compat.ic2.CompatModuleIC2;
import com.amadornes.framez.compat.nei.CompatModuleNEI;
import com.amadornes.framez.compat.oc.CompatModuleOC;
import com.amadornes.framez.compat.pc.CompatModulePC;
import com.amadornes.framez.compat.rf.CompatModuleRF;
import com.amadornes.framez.compat.tf.CompatModuleTF;
import com.amadornes.framez.compat.waila.CompatModuleWAILA;

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

    public static void prePreInit() {

        for (CompatModule m : getLoadedModules())
            m.prePreInit();
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

    public static IFramezWrench getWrenchFromStack(ItemStack stack) {

        for (CompatModule m : getLoadedModules()) {
            IFramezWrench w = m.getWrench(stack);
            if (w != null)
                return w;
        }

        return null;
    }

    /**
     * Register modules here
     */
    static {
        if (FramezCompatConfig.ae2_enabled)
            registerModule(Dependencies.AE2, CompatModuleAE2.class);
        if (FramezCompatConfig.bc_enabled)
            registerModule(Dependencies.BC, CompatModuleBC.class);
        if (FramezCompatConfig.botania_enabled)
            registerModule(Dependencies.BOTANIA, CompatModuleBotania.class);
        if (FramezCompatConfig.cc_enabled)
            registerModule(Dependencies.CC, CompatModuleCC.class);
        if (FramezCompatConfig.cofhlib_enabled)
            registerModule(Dependencies.COFH_LIB, CompatModuleCoFHLib.class);
        if (FramezCompatConfig.ee3_enabled)
            registerModule(Dependencies.EE3, CompatModuleEE3.class);
        if (FramezCompatConfig.floco_enabled)
            registerModule(Dependencies.FLOCO, CompatModuleFLoco.class);
        if (FramezCompatConfig.fmp_enabled)
            registerModule(Dependencies.FMP, CompatModuleFMP.class);
        if (FramezCompatConfig.ic2_enabled)
            registerModule(Dependencies.IC2, CompatModuleIC2.class);
        if (FramezCompatConfig.nei_enabled)
            registerModule(Dependencies.NEI, CompatModuleNEI.class);
        if (FramezCompatConfig.oc_enabled)
            registerModule(Dependencies.OC, CompatModuleOC.class);
        if (FramezCompatConfig.pc_enabled)
            registerModule(Dependencies.PC, CompatModulePC.class);
        if (FramezCompatConfig.rf_enabled)
            registerModule("api:" + Dependencies.API_RF, CompatModuleRF.class);
        if (FramezCompatConfig.tf_enabled)
            registerModule(Dependencies.TF, CompatModuleTF.class);
        if (FramezCompatConfig.waila_enabled)
            registerModule(Dependencies.WAILA, CompatModuleWAILA.class);

    }

}
