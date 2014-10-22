package com.amadornes.framez.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IMotor;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.MotorPlacement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMotor extends BlockContainer implements IMotor {

    private IMotorProvider provider;

    private MovingObjectPosition placeMOP = null;

    public BlockMotor(IMotorProvider provider) {

        super(Material.iron);
        this.provider = provider;

        setBlockName(References.Names.Registry.MOTOR);
        setHardness(2);

        setCreativeTab(CreativeTabFramez.inst);
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
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMotor.RENDER_ID;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

        if (!world.isRemote) {
            TileMotor te = (TileMotor) world.getTileEntity(x, y, z);

            te.setFace(te.getFace().getRotation(axis));
            te.setDirection(te.getDirection().getRotation(axis));
            te.sendUpdatePacket();
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {

        super.onBlockPlacedBy(world, x, y, z, entity, item);

        TileMotor te = (TileMotor) world.getTileEntity(x, y, z);
        if (entity instanceof EntityPlayer) {
            te.setPlacer(((EntityPlayer) entity).getGameProfile().getName());

            ForgeDirection face = ForgeDirection.getOrientation(placeMOP.sideHit).getOpposite();
            te.setFace(face, true);
            te.setDirection(MotorPlacement.getPlacementDirection(placeMOP, face), true);
        }
        te.sendUpdatePacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = reg.registerIcon(References.Textures.MOTOR);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        return true;
    }

    @Override
    public String getUnlocalizedName() {

        return "tile." + provider.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess w, int x, int y, int z) {

        if (!RenderMotor.renderingBorder)
            return 0xFFFFFF;

        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMotor)
            return ((TileMotor) te).getColorMultiplier();

        return 0xFF0000;
    }

    @Override
    public IMotorProvider getProvider() {

        return provider;
    }

    @Override
    public int onBlockPlaced(World w, int x, int y, int z, int side, float x_, float y_, float z_, int meta) {

        // TileMotor te = (TileMotor) w.getTileEntity(x, y, z);
        // if (te == null)
        // w.setTileEntity(x, y, z, te = (TileMotor) createNewTileEntity(w, meta));

        /* MovingObjectPosition mop = */placeMOP = new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(x + x_, y + y_, z + z_));

        // ForgeDirection face = ForgeDirection.getOrientation(mop.sideHit).getOpposite();
        // te.setFace(face);
        // te.setDirection(MotorPlacement.getPlacementDirection(mop, face));
        // te.sendUpdatePacket();

        return super.onBlockPlaced(w, x, y, z, side, x_, y_, z_, meta);
    }
}
