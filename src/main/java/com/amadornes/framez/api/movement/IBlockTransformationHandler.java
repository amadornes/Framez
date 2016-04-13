package com.amadornes.framez.api.movement;

import java.util.Set;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockTransformationHandler {

    public boolean canTransform(IBlockAccess world, BlockPos pos, Set<BlockPos> movedBlocks, IBlockTransformation... transformations);

    public void transformBlock(World world, BlockPos pos, Set<BlockPos> movedBlocks, IBlockTransformation... transformations);

}
