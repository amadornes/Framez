package com.amadornes.framez.init;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.item.ItemCrafting;
import com.amadornes.framez.item.ItemIngotSpecoriumLove;
import com.amadornes.framez.item.ItemIronNugget;
import com.amadornes.framez.item.ItemMotorCore;
import com.amadornes.framez.item.ItemUpgrade;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezItems {

    public static Item wrench;
    public static Item iron_nugget;
    public static Item upgrade_bouncy;
    public static Item upgrade_camouflage;
    public static Item upgrade_momentum_conservation;
    public static Item specorium_love;
    public static Item motor_core;
    public static Map<IFrameMaterial, Item> crafting = new LinkedHashMap<IFrameMaterial, Item>();

    public static void init() {

        try {
            wrench = Framez.instance.wrenchItemClass.newInstance();
            wrench.setFull3D().setMaxStackSize(1).setCreativeTab(FramezCreativeTab.tab).setHarvestLevel("wrench", 0);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize the wrench. Something went horribly wrong with mod compatibility!", e);
        }
        iron_nugget = new ItemIronNugget();
        upgrade_bouncy = new ItemUpgrade("bouncy");
        upgrade_camouflage = new ItemUpgrade("camouflage");
        upgrade_momentum_conservation = new ItemUpgrade("momentum_conservation");
        specorium_love = new ItemIngotSpecoriumLove();
        motor_core = new ItemMotorCore();

        for (IFrameMaterial m : ModifierRegistry.instance.frameMaterials)
            crafting.put(m, new ItemCrafting(m));
    }

    public static void register() {

        GameRegistry.registerItem(wrench, "wrench");
        GameRegistry.registerItem(iron_nugget, "iron_nugget");
        GameRegistry.registerItem(upgrade_bouncy, "upgrade_bouncy");
        GameRegistry.registerItem(upgrade_camouflage, "upgrade_camouflage");
        GameRegistry.registerItem(upgrade_momentum_conservation, "upgrade_momentum_conservation");
        GameRegistry.registerItem(specorium_love, "specorium_love");
        GameRegistry.registerItem(motor_core, "motor_core");

        for (Entry<IFrameMaterial, Item> e : crafting.entrySet())
            GameRegistry.registerItem(e.getValue(), "crafting_" + e.getKey().getType());
    }
}
