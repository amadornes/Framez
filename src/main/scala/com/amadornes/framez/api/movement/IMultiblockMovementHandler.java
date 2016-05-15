package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.BlockSet;

public interface IMultiblockMovementHandler {

    public void addBlocks(World world, BlockSet blocks);

}
