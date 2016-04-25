package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFramez extends Item implements IFramezItem {

    private final String name;

    public ItemFramez(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isBlock() {

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return IFramezItem.super.getUnlocalizedName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {

        IFramezItem.super.addInformation(stack, playerIn, tooltip, advanced);
    }

}
