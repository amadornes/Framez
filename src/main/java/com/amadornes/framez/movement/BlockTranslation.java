package com.amadornes.framez.movement;

import com.amadornes.framez.api.movement.IBlockTransformation.IBlockTranslation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTranslation implements IBlockTranslation {

    private final BlockPos translation;

    public BlockTranslation(BlockPos translation) {

        this.translation = translation;
    }

    @Override
    public String getType() {

        return "translation";
    }

    @Override
    public void defaultTransform(World world, BlockPos pos) {

        IBlockState state = world.getBlockState(pos);
        BlockPos transformedPos = transform(pos);

        IBlockState oldState = world.getBlockState(transformedPos);
        world.setBlockState(transformedPos, state, 0);
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            world.removeTileEntity(pos);
        }
        world.setBlockToAir(pos);
        if (tile != null) {
            tile.setWorldObj(world);
            tile.setPos(transformedPos);
            tile.validate();
            world.setTileEntity(transformedPos, tile);
            tile.updateContainingBlockInfo();
        }
        world.markAndNotifyBlock(transformedPos, world.getChunkFromBlockCoords(transformedPos), oldState, state, 3);
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        return pos.add(translation);
    }

    @Override
    public Vec3 transform(Vec3 vec, boolean normal) {

        return normal ? vec : vec.addVector(translation.getX(), translation.getY(), translation.getZ());
    }

    @Override
    public EnumFacing transform(EnumFacing facing) {

        return facing;
    }

    @Override
    public BlockPos getTranslation() {

        return translation;
    }

}
