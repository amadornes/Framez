package com.amadornes.framez.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class BlockMotor extends BlockContainer {

    private IMotorProvider provider;

    public BlockMotor(IMotorProvider provider) {

        super(Material.iron);
        this.provider = provider;

        setBlockName(References.MOTOR_NAME);
        setHardness(2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        try {
            return provider.getTileClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
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
    public int getRenderType() {

        return RenderMotor.RENDER_ID;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

        TileMotor te = (TileMotor) world.getTileEntity(x, y, z);

        te.setFace(te.getFace().getRotation(axis));
        te.setDirection(te.getDirection().getRotation(axis));

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {

        super.onBlockPlacedBy(world, x, y, z, entity, item);

        TileMotor te = (TileMotor) world.getTileEntity(x, y, z);
        if (entity instanceof EntityPlayer)
            te.setPlacer(((EntityPlayer) entity).getGameProfile().getName());
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = reg.registerIcon(ModInfo.MODID + ":motor");
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return true;
    }

    @Override
    public String getUnlocalizedName() {

        return provider.getUnlocalizedName();
    }

    @Override
    public void randomDisplayTick(World w, int x, int y, int z, Random rnd) {

        ((TileMotor) w.getTileEntity(x, y, z)).randomDisplayTick(rnd);
    }

    @Override
    public int colorMultiplier(IBlockAccess w, int x, int y, int z) {

        if (!RenderMotor.renderingBorder)
            return 0xFFFFFF;

        return ((TileMotor) w.getTileEntity(x, y, z)).getColorMultiplier();
    }

}
