package com.amadornes.framez.init;

import org.apache.commons.lang3.StringUtils;

import com.amadornes.framez.item.ItemNugget;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FramezOredict {

    public static void register() {

        int i = 0;
        for (String type : ItemNugget.NUGGET_TYPES)
            OreDictionary.registerOre("nugget" + StringUtils.capitalize(type), new ItemStack(FramezItems.nugget, 1, i++));
    }

}
