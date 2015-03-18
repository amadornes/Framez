package com.amadornes.framez.init;

import net.minecraft.item.Item;

import com.amadornes.framez.item.ItemPartFrame;
import com.amadornes.framez.item.ItemWrench;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezItems {

    public static Item frame;
    public static Item wrench;

    public static void init() {

        frame = new ItemPartFrame();
        wrench = new ItemWrench();
    }

    public static void register() {

        GameRegistry.registerItem(frame, References.Item.FRAME);
        GameRegistry.registerItem(wrench, References.Item.WRENCH);
    }

}
