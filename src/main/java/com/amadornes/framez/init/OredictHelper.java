package com.amadornes.framez.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OredictHelper {

    public static final void init() {

        if (OreDictionary.getOres("glass").size() == 0) {
            OreDictionary.registerOre("glass", Blocks.glass);
            OreDictionary.registerOre("glass", new ItemStack(Blocks.stained_glass, 1, Short.MAX_VALUE));
        }
        if (OreDictionary.getOres("glassPane").size() == 0) {
            OreDictionary.registerOre("glassPane", Blocks.glass_pane);
            OreDictionary.registerOre("glassPane", new ItemStack(Blocks.stained_glass_pane, 1, Short.MAX_VALUE));
        }
        if (FramezItems.ironstick != null)
            OreDictionary.registerOre("stickIron", FramezItems.ironstick);
    }

}
