package com.amadornes.framez.api.movement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMovingBlock {

    public World getWorld();

    public BlockPos getPos();

    public IBlockState getState();

    public TileEntity getTile();

}
