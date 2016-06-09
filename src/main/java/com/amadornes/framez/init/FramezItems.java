package com.amadornes.framez.init;

import java.util.ArrayList;
import java.util.function.IntFunction;

import com.amadornes.framez.Framez;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.item.ItemFrame;
import com.amadornes.framez.item.ItemFramePanel;
import com.amadornes.framez.item.ItemIcons;
import com.amadornes.framez.item.ItemMotorUpgrade;
import com.amadornes.framez.item.ItemNugget;
import com.amadornes.framez.item.ItemWrench;
import com.amadornes.framez.motor.MotorRegistry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FramezItems {

    public static Item frame;
    public static Item wrench;
    public static Item upgrade;
    public static Item nugget;
    public static Item frame_panel;
    public static Item icons;

    public static void initialize() {

        frame = new ItemFrame();
        wrench = new ItemWrench();
        upgrade = new ItemMotorUpgrade();
        nugget = new ItemNugget();
        frame_panel = new ItemFramePanel();
        icons = new ItemIcons();
    }

    public static void register() {

        registerItem(frame, "frame");
        registerItem(wrench, "wrench");
        registerItem(upgrade, "upgrade", i -> "_" + new ArrayList<String>(MotorRegistry.INSTANCE.internalUpgrades.keySet()).get(i),
                MotorRegistry.INSTANCE.internalUpgrades.size());
        registerItem(nugget, "nugget", i -> "_" + ItemNugget.NUGGET_TYPES.get(i), ItemNugget.NUGGET_TYPES.size());
        registerItem(frame_panel, "frame_panel", 3);
        registerItem(icons, "icons", EnumMotorAction.values().length);
    }

    private static void registerItem(Item item, String name, int variantCount) {

        registerItem(item, name, i -> i + "", variantCount);
    }

    private static void registerItem(Item item, String name, IntFunction<String> i2s, int variantCount) {

        GameRegistry.register(item, new ResourceLocation(ModInfo.MODID, name));
        for (int i = 0; i < variantCount; i++)
            Framez.proxy.registerItemRenderer(item, name, i2s, i);
        item.setCreativeTab(FramezCreativeTab.tab);
    }

    private static void registerItem(Item item, String name, int... variants) {

        registerItem(item, name, i -> i + "", variants);
    }

    private static void registerItem(Item item, String name, IntFunction<String> i2s, int... variants) {

        GameRegistry.register(item, new ResourceLocation(ModInfo.MODID, name));
        if (variants.length <= 1) {
            Framez.proxy.registerItemRenderer(item, name, i2s, -1);
        } else {
            for (int v : variants)
                Framez.proxy.registerItemRenderer(item, name, i2s, v);
        }
        item.setCreativeTab(FramezCreativeTab.tab);
    }

}
