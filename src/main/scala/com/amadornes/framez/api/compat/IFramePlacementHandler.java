package com.amadornes.framez.api.compat;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.trajectory.api.vec.BlockPos;

public interface IFramePlacementHandler {

    public int priority();

    public boolean placeFrame(World world, BlockPos position, IFrame reference);

    public boolean canPlaceFrame(World world, BlockPos position, IFrame reference);

    // public static interface IGhostFramePlacementHandler {
    //
    // public boolean canPlaceGhostFrame(World world, BlockPos position);
    // }

}
