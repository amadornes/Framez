package com.amadornes.framez.block;

import com.amadornes.framez.tile.TileMotorExtension;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMotorExtension extends Block implements ITileEntityProvider {

    public BlockMotorExtension() {

        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileMotorExtension();
    }

}
