package com.amadornes.framez.init;

import net.minecraft.item.Item;

import com.amadornes.framez.item.ItemFramePart;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MODID)
public final class FramezItems {

    public static Item item_frame_part;

    public static final void init() {

        instantiate();
        register();
    }

    private static final void instantiate() {

        item_frame_part = new ItemFramePart();
    }

    private static final void register() {

        GameRegistry.registerItem(item_frame_part, References.FRAME_PART_ITEM_NAME);
    }

}
