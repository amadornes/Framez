package com.amadornes.framez.item;

import java.util.List;

import com.amadornes.framez.frame.FrameRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemFramePanel extends ItemFramez {

    public ItemFramePanel() {

        super("frame_panel");
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

        for (ResourceLocation material : FrameRegistry.INSTANCE.getMaterials().keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("cross", material.toString());
            tag.setString("binding", material.toString());
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(tag);
            list.add(stack);
        }

        for (ResourceLocation material : FrameRegistry.INSTANCE.getMaterials().keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("border", material.toString());
            tag.setString("cross", material.toString());
            tag.setString("binding", material.toString());
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(tag);
            list.add(stack);
        }
    }

}
