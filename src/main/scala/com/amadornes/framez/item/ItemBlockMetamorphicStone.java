package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.ref.ModInfo;

public class ItemBlockMetamorphicStone extends ItemBlockFramez {

    public ItemBlockMetamorphicStone(Block block) {

        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        int meta = stack.getItemDamage();

        return "tile."
                + ModInfo.MODID
                + ":metamorphic_stone"
                + (meta == 0 ? "" : (meta == 1 ? ".cracked" : (meta == 2 ? ".bricks" : (meta == 3 ? ".water" : (meta == 4 ? ".fire"
                        : (meta == 5 ? ".ice" : ""))))));
    }

    @Override
    protected String getUnlocalizedTip(ItemStack stack, EntityPlayer player) {

        return super.getUnlocalizedTip(stack, player);
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item i, CreativeTabs t, List l) {

        l.add(new ItemStack(i, 1, 0));
        l.add(new ItemStack(i, 1, 1));
        l.add(new ItemStack(i, 1, 2));
        l.add(new ItemStack(i, 1, 3));
        l.add(new ItemStack(i, 1, 4));
        l.add(new ItemStack(i, 1, 5));
    }

    @Override
    public int getMetadata(int itemMeta) {

        return itemMeta;
    }

}