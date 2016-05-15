package com.amadornes.framez.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class FramezDocumentation {

    private static final Map<ItemStack, String> documentation = new HashMap<ItemStack, String>();

    public static void addDocumentation(ItemStack stack, String docu) {

        documentation.put(stack, docu);
    }

    public static String getDocumentation(ItemStack stack) {

        String unlocalized = stack.getItem().getUnlocalizedName(stack);
        if (unlocalized.startsWith("item.") || unlocalized.startsWith("tile."))
            unlocalized = "docu." + unlocalized.substring(5);
        String localized = I18n.format(unlocalized);

        if (unlocalized.equals(localized))
            return null;
        return localized;
    }

}
