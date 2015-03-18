package com.amadornes.framez.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.client.RenderMotor;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMotorSlider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMotor extends BlockContainer {

    public BlockMotor() {

        super(Material.iron);

        setBlockName(References.Block.MOTOR);
    }

    @Override
    public String getUnlocalizedName() {

        return "tile." + ModInfo.MODID + ":" + References.Block.MOTOR;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        TileMotor motor = MotorFactory.createMotor(TileMotorSlider.class, "motor");

        return motor;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;

        ((TileMotor) tile).onBlockUpdate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMotor.RENDER_ID;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

        if (world.isRemote)
            return true;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;

        return ((TileMotor) tile).rotate(axis);
    }

    @Override
    public boolean isNormalCube() {

        return super.isNormalCube();
    }

    @Override
    public boolean isOpaqueCube() {

        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {

        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return true;
    }

}
