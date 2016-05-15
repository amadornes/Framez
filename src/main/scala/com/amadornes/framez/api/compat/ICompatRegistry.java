package com.amadornes.framez.api.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.trajectory.api.vec.BlockPos;

public interface ICompatRegistry {

    public IFMPCompatRegistry fmp();

    public IFramezWrench getModdedWrench(ItemStack stack);

    public boolean placeFrame(World world, BlockPos position, ItemStack stack);

    public void registerFramePlacementHandler(IFramePlacementHandler handler);

    // public void registerGhostFramePlacementHandler(IGhostFramePlacementHandler handler);

}
