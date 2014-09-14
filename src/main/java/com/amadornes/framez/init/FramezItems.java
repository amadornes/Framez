package com.amadornes.framez.init;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import com.amadornes.framez.item.ItemFrameMuptipart;
import com.amadornes.framez.item.ItemFramePart;
import com.amadornes.framez.item.ItemStickIron;
import com.amadornes.framez.item.ItemWrench;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MODID)
public final class FramezItems {

    public static Item frame;
    public static Item wrench;

    public static Item framepart;

    public static Item ironstick;

    public static final void init() {

        instantiate();
        register();
    }

    private static final void instantiate() {

        frame = new ItemFrameMuptipart();
        wrench = new ItemWrench();
        framepart = new ItemFramePart();

        if (OreDictionary.getOres("stickIron").size() == 0)
            ironstick = new ItemStickIron();
    }

    private static final void register() {

        GameRegistry.registerItem(frame, References.Names.Registry.FRAME);
        GameRegistry.registerItem(wrench, References.Names.Registry.WRENCH);
        GameRegistry.registerItem(framepart, References.Names.Registry.FRAME_PART);
        if (ironstick != null)
            GameRegistry.registerItem(ironstick, References.Names.Registry.IRON_STICK);
    }

}
