package com.amadornes.framez.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBreaker extends BlockDirectional implements ITileEntityProvider {

    public BlockBreaker() {

        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return null;
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer.Builder(this).add(FACING).build();
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer) {

        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer)), 2);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

        return layer == BlockRenderLayer.CUTOUT;
    }

}
