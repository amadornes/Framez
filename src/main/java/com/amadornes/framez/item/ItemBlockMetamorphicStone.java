package com.amadornes.framez.item;

import java.util.List;

import com.amadornes.framez.ModInfo;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlockMetamorphicStone extends ItemBlockFramez {

    public ItemBlockMetamorphicStone(Block block) {

        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        int meta = stack.getItemDamage();

        return "tile." + ModInfo.MODID + ":metamorphic_stone"
                + (meta == 0 ? ""
                        : (meta == 1 ? ".cracked"
                                : (meta == 2 ? ".bricks" : (meta == 3 ? ".water" : (meta == 4 ? ".fire" : (meta == 5 ? ".ice" : ""))))));
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> items) {

        items.add(new ItemStack(item, 1, 0));
        items.add(new ItemStack(item, 1, 1));
        items.add(new ItemStack(item, 1, 2));
        items.add(new ItemStack(item, 1, 3));
        items.add(new ItemStack(item, 1, 4));
        items.add(new ItemStack(item, 1, 5));
    }

    @Override
    public int getMetadata(int itemMeta) {

        return itemMeta;
    }

}