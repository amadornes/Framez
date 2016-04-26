package com.amadornes.framez.api.movement;

import java.util.Map;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IMovementHandler {

    public boolean canHandle(IMovingBlock block);

    public boolean canRelocateBlock(IMovingBlock block, World destWorld, BlockPos destPos, Map<IMovingBlock, BlockPos> relocatedBlocks);

    public void relocateBlock(IMovingBlock block, World destWorld, BlockPos destPos, boolean notify);

    public boolean canRotateBlock(IMovingBlock block, EnumFacing axis, Map<IMovingBlock, BlockPos> rotatedBlocks);

    public void rotateBlock(IMovingBlock block, EnumFacing axis, boolean notify);

}
