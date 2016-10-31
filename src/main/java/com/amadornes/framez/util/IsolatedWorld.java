package com.amadornes.framez.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class IsolatedWorld implements IBlockAccess {

    private static final IsolatedWorld INSTANCE = new IsolatedWorld();

    public static IsolatedWorld getWorld(IBlockAccess world, BlockPos pos) {

        INSTANCE.world = world;
        INSTANCE.position = pos;
        return INSTANCE;
    }

    private IsolatedWorld() {
    }

    private IBlockAccess world;
    private BlockPos position;

    @Override
    public TileEntity getTileEntity(BlockPos pos) {

        if (this.position.equals(pos)) {
            return world.getTileEntity(pos);
        }
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {

        return world.getCombinedLight(pos, lightValue);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {

        if (this.position.equals(pos)) {
            return world.getBlockState(pos);
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {

        if (this.position.equals(pos)) {
            return world.isAirBlock(pos);
        }
        return true;
    }

    @Override
    public Biome getBiome(BlockPos pos) {

        return world.getBiome(pos);
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {

        if (this.position.equals(pos)) {
            return world.getStrongPower(pos, direction);
        }
        return 0;
    }

    @Override
    public WorldType getWorldType() {

        return world.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {

        if (this.position.equals(pos)) {
            return world.isSideSolid(pos, side, _default);
        }
        return false;
    }

}
