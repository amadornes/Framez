package com.amadornes.framez.movement;

import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.api.movement.IBlockTransformation;
import com.amadornes.framez.api.movement.IBlockTransformationHandler;
import com.google.common.base.Predicate;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SimpleMovementSystem implements IMovementSystem {

    @Override
    public boolean canMove(IBlockAccess world, Set<BlockPos> movedBlocks, IBlockTransformation... transformations) {

        for (BlockPos pos : movedBlocks)
            for (Pair<Pair<Integer, Predicate<Pair<IBlockAccess, BlockPos>>>, IBlockTransformationHandler> p : BlockTransformationRegistry.INSTANCE.handlers)
                if (p.getKey().getValue().apply(Pair.of(world, pos)) && p.getValue().canTransform(world, pos, movedBlocks, transformations))
                    return true;
        return false;
    }

    @Override
    public IMovingStructure startMoving(World world, Set<BlockPos> movedBlocks, int ticks, IBlockTransformation... transformations) {

        for (BlockPos pos : movedBlocks)
            for (Pair<Pair<Integer, Predicate<Pair<IBlockAccess, BlockPos>>>, IBlockTransformationHandler> p : BlockTransformationRegistry.INSTANCE.handlers)
                if (p.getKey().getValue().apply(Pair.of(world, pos)) && p.getValue().canTransform(world, pos, movedBlocks, transformations))
                    p.getValue().transformBlock(world, pos, movedBlocks, transformations);
        return null;
    }

}
