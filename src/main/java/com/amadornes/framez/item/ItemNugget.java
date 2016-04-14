package com.amadornes.framez.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNugget extends ItemFramez {

    public static List<String> NUGGET_TYPES = new ArrayList<String>();

    public ItemNugget() {

        super("nugget");
        NUGGET_TYPES.add("iron");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return super.getUnlocalizedName(stack) + "." + NUGGET_TYPES.get(stack.getItemDamage());
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

        int i = 0;
        for (String type : NUGGET_TYPES)
            subItems.add(new ItemStack(itemIn, 1, i++));
    }

}
