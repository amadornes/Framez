package com.amadornes.framez.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.Framez;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMoving extends BlockContainer {

    public BlockMoving() {

        super(Material.rock);

        setBlockName(References.Names.Registry.MOVING);

        setHardness(-1);
        setResistance(-1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getUnlocalizedName() {

        return "Moving";
    }

    @Override
    public TileEntity createNewTileEntity(World w, int meta) {

        return new TileMoving();
    }

    private TileMoving get(IBlockAccess w, int x, int y, int z) {

        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMoving)
            return (TileMoving) te;

        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB aabb, List l, Entity e) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return;

        te.addCollisionBoxesToList(aabb, l, e);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);

        return te.getSelectedBoundingBox();
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return null;

        return te.rayTrace(start, end);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return -1;
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
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float subX, float subY, float subZ) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return false;

        return te.onBlockActivated(p);
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rnd) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return;
        if (te.getBlockA() == null)
            return;
        te.getBlockA().getBlock().updateTick(te.getBlockA().getWorldWrapper(), x, y, z, rnd);
    }

    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return 0;
        return te.getLightValue();
    }

    @Override
    public int getLightOpacity(IBlockAccess w, int x, int y, int z) {

        return 0;
        // TileMoving te = get(w, x, y, z);
        // if (te == null)
        // return 0;
        // return te.getLightOpacity();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World w, int x, int y, int z) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return null;
        return te.getPickBlock(target);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World w, int x, int y, int z, Random rnd) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return;
        te.randomDisplayTick(rnd);
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {

    }

    private boolean drawingHighlight = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDrawHighlight(DrawBlockHighlightEvent event) {

        if (event == null || event.context == null || event.currentItem == null || event.player == null || event.target == null
                || event.target.typeOfHit != MovingObjectType.BLOCK)
            return;

        if (drawingHighlight)
            return;

        drawingHighlight = true;

        TileMoving te = get(event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ);
        if (te != null) {
            MovingObjectPosition mop = te.rayTrace(event.player);

            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                DrawBlockHighlightEvent ev = new DrawBlockHighlightEvent(event.context, event.player, mop, mop.subHit, event.currentItem,
                        event.partialTicks);
                World world = event.player.worldObj;
                World w = null;
                double moved = 0;
                ForgeDirection dir = null;
                MovingBlock a = te.getBlockA();
                MovingBlock b = te.getBlockB();
                if (a != null) {
                    w = a.getWorldWrapper();
                    moved = (a.getMoved() - a.getSpeed() * (1 - Framez.proxy.getFrame()));
                    dir = a.getDirection();
                } else if (b != null) {
                    w = b.getWorldWrapper();
                    moved = (b.getMoved() - b.getSpeed() * (1 - Framez.proxy.getFrame()));
                    b.getDirection();
                }

                if (w != null) {
                    event.player.worldObj = w;

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslated(dir.offsetX * moved, dir.offsetY * moved, dir.offsetZ * moved);
                        MinecraftForge.EVENT_BUS.post(ev);
                    }
                    GL11.glPopMatrix();

                    if (ev.isCanceled())
                        event.setCanceled(true);

                    event.player.worldObj = world;
                }
            }
        }

        drawingHighlight = false;
    }

}
