package com.amadornes.framez.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.amadornes.framez.ModInfo;

public class FramezCreativeTab extends CreativeTabs {

    public static final FramezCreativeTab tab = new FramezCreativeTab();

    public FramezCreativeTab() {

        super(ModInfo.MODID);
    }

    @Override
    public Item getTabIconItem() {

        return Items.apple;
    }

    // @Override
    // public ItemStack getIconItemStack() {
    //
    // int t = 1000;
    // int i = (int) ((System.currentTimeMillis() / t) % FramezBlocks.frames.size());
    // return new ItemStack(new ArrayList<BlockFrame>(FramezBlocks.frames.values()).get(i));
    // }

}
