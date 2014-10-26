package com.amadornes.framez.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

public class ItemBlockMotorCore extends ItemBlock {

    public ItemBlockMotorCore(Block block) {

        super(block);
    }

    @Override
    public String getUnlocalizedName() {

        return "item." + ModInfo.MODID + ":" + References.Names.Unlocalized.MOTORCORE;
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World w, int x, int y, int z, int side, float x_, float y_, float z_) {

        return false;
    }

}
