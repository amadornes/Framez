package com.amadornes.framez.item;

import com.amadornes.framez.ModInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends Item {

    public ItemWrench() {

        setUnlocalizedName(ModInfo.MODID + ":wrench");
    }

    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer player) {

        return true;
    }

}
