package com.amadornes.framez.movement;

import java.util.Set;

import com.amadornes.framez.api.movement.IBlockTransformation;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMovementSystem {

    public static final IMovementSystem SYSTEM = new SimpleMovementSystem();

    public boolean canMove(IBlockAccess world, Set<BlockPos> movedBlocks, IBlockTransformation... transformations);

    public IMovingStructure startMoving(World world, Set<BlockPos> movedBlocks, int ticks, IBlockTransformation... transformations);

    public static interface IMovingStructure {

        public float getProgress();

        public Set<BlockPos> getBlocks();

        public void tick();

    }

}
