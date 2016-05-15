package com.amadornes.framez.api.wrench;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

public interface IFramePartHandler {

    public IFramePart[] silkHarvest(World world, BlockPos position, boolean simulated);

    public IFramePart createLocated(int relX, int relY, ItemStack stack);

    public IFramePart createPart(String type, boolean client);

}
