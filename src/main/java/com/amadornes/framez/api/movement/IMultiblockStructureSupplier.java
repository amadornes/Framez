package com.amadornes.framez.api.movement;

import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiblockStructureSupplier {

    public void addBlocks(World world, Set<IMovingBlock> movedBlocks, Set<BlockPos> extraBlocks);

}
