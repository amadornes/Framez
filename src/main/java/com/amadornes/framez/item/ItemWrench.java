package com.amadornes.framez.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends ItemFramez {

    public ItemWrench() {

        super("wrench");
    }

    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer player) {

        return true;
    }

}
