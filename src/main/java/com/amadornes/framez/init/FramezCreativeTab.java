package com.amadornes.framez.init;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.framez.ModInfo;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FramezCreativeTab extends CreativeTabs {

    public static final FramezCreativeTab tab = new FramezCreativeTab();

    public FramezCreativeTab() {

        super(ModInfo.MODID);
    }

    @Override
    public Item getTabIconItem() {

        return Items.APPLE;
    }

    @Override
    public ItemStack getIconItemStack() {

        int t = 2500;
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        FramezItems.frame.getSubItems(FramezItems.frame, this, stacks);
        return stacks.get((int) ((System.currentTimeMillis() / t) % stacks.size()));
    }

}
