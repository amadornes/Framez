package com.amadornes.framez.init;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.ref.ModInfo;

public class FramezCreativeTab extends CreativeTabs {

    public static final FramezCreativeTab tab = new FramezCreativeTab();

    public FramezCreativeTab() {

        super(ModInfo.MODID);
    }

    @Override
    public Item getTabIconItem() {

        return null;
    }

    @Override
    public ItemStack getIconItemStack() {

        int t = 1000;
        int i = (int) ((System.currentTimeMillis() / t) % FramezBlocks.frames.size());
        return new ItemStack(new ArrayList<BlockFrame>(FramezBlocks.frames.values()).get(i));
    }

}
