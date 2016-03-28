package com.amadornes.framez.movement;

import java.util.Set;

import com.amadornes.framez.api.movement.IBlockTransformation;
import com.amadornes.framez.api.movement.IBlockTransformationHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DefaultBlockTransformationHandler implements IBlockTransformationHandler {

    @Override
    public boolean canTransform(IBlockAccess world, BlockPos pos, Set<BlockPos> movedBlocks, IBlockTransformation... transformations) {

        IBlockState state = world.getBlockState(pos);
        return state.getBlock().blockHardness >= 0 && !state.getBlock().isAir(world, pos);
    }

    @Override
    public void transformBlock(World world, BlockPos pos, Set<BlockPos> movedBlocks, IBlockTransformation... transformations) {

        for (IBlockTransformation t : transformations) {
            t.defaultTransform(world, pos);
            pos = t.transform(pos);
        }
    }

}
