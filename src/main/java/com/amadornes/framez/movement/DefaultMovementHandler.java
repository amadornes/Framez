package com.amadornes.framez.movement;

import java.util.Map;

import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DefaultMovementHandler implements IMovementHandler {

    @Override
    public boolean canHandle(IMovingBlock block) {

        return true;
    }

    @Override
    public boolean canRelocateBlock(IMovingBlock block, World destWorld, BlockPos destPos, Map<IMovingBlock, BlockPos> relocatedBlocks) {

        return !block.getState().getBlock().isAir(block.getWorld(), block.getPos())
                && (block.getTile() == null || block.getTile().getClass().getName().startsWith("net.minecraft."));
    }

    @Override
    public void relocateBlock(IMovingBlock block, World destWorld, BlockPos destPos, boolean notify) {

        IBlockState oldState = destWorld.getBlockState(destPos);
        destWorld.setBlockState(destPos, block.getState(), 0);
        if (block.getState().getBlock().hasTileEntity(block.getState())) destWorld.removeTileEntity(destPos);
        TileEntity tile = block.getTile();
        if (tile != null) block.getWorld().removeTileEntity(block.getPos());
        block.getWorld().setBlockToAir(block.getPos());
        if (tile != null) {
            tile.setWorldObj(destWorld);
            tile.setPos(destPos);
            tile.validate();
            destWorld.setTileEntity(destPos, tile);
            tile.updateContainingBlockInfo();
        }
        destWorld.markAndNotifyBlock(destPos, destWorld.getChunkFromBlockCoords(destPos), oldState, block.getState(), 3);
    }

    @Override
    public boolean canRotateBlock(IMovingBlock block, EnumFacing axis, Map<IMovingBlock, BlockPos> rotatedBlocks) {

        return true;
    }

    @Override
    public void rotateBlock(IMovingBlock block, EnumFacing axis, boolean notify) {

        block.getState().getBlock().rotateBlock(block.getWorld(), block.getPos(), axis);
    }

}
