package com.amadornes.framez.api.wrench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

public interface IFramezWrench {

    public WrenchAction getWrenchAction(ItemStack stack);

    public void onUsed(ItemStack stack, EntityPlayer player);

    public boolean isSilky(ItemStack stack);

    public ItemStack silkPick(World world, BlockPos position, boolean simulated);

    public static enum WrenchAction {
        DEFAULT, ROTATE, DEBUG, CONFIG, NONE;
    }

}
