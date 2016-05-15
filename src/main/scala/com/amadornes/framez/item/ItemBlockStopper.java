package com.amadornes.framez.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.ref.ModInfo;

public class ItemBlockStopper extends ItemBlockFramez {

    public ItemBlockStopper(Block block) {

        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "tile." + ModInfo.MODID + ":stopper." + stack.getItemDamage();
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public int getMetadata(int itemMeta) {

        return itemMeta;
    }

}