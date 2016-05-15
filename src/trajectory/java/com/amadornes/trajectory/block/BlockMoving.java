package com.amadornes.trajectory.block;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.world.FakeWorld;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMoving extends BlockContainer {

    public BlockMoving() {

        super(Material.rock);

        setBlockName("moving");

        setHardness(-1);
        setResistance(-1);

        MinecraftForge.EVENT_BUS.register(this);
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
        if (te == null) {
            w.setBlockToAir(x, y, z);
            return;
        }

        te.addCollisionBoxesToList(aabb, l, e);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        return super.getSelectedBoundingBoxFromPool(w, x, y, z);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return null;

        MovingObjectPosition mop = te.rayTrace(new Vector3(start), new Vector3(end));
        if (mop != null && mop.hitInfo instanceof Pair<?, ?>)
            mop.hitInfo = ((Pair<?, ?>) mop.hitInfo).getValue();
        return mop;
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

        // World fw = FakeWorld.getFakeWorld(te.structure);
        // World rw = p.worldObj;
        // p.worldObj = fw;
        //
        // ItemStack item = p.getCurrentEquippedItem();
        //
        // if (item != null) {
        // if (item.getItem().onItemUseFirst(item, p, fw, x, y, z, side, subX, subY, subZ)) {
        // p.worldObj = rw;
        // return true;
        // }
        // }
        //
        // if (te.onBlockActivated(p)) {
        // p.worldObj = rw;
        // return true;
        // }
        //
        // if (item != null) {
        // if (item.getItem().onItemUse(item, p, fw, x, y, z, side, subX, subY, subZ)) {
        // p.worldObj = rw;
        // return true;
        // }
        // }
        //
        // return false;
        return te.onBlockActivated(p);
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rnd) {

        // TileMoving te = get(w, x, y, z);
        // if (te == null)
        // return;
        // if (te.getBlockA() == null)
        // return;
        // te.getBlockA().getBlock().updateTick(FakeWorld.getFakeWorld(te.getBlockA()), x, y, z, rnd);
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {

        TileMoving te = get(world, x, y, z);
        if (te == null)
            return null;
        return te.getPickBlock(target, player);
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
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {

    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {

        return false;
    }

    @Override
    public boolean canDropFromExplosion(Explosion p_149659_1_) {

        return false;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {

        TileMoving te = get(world, x, y, z);
        if (te == null)
            return false;

        MovingObjectPosition mop = te.rayTrace(player);
        if (mop != null) {
            MovingBlock block = (MovingBlock) ((Entry<?, ?>) mop.hitInfo).getKey();
            boolean result = block.getBlock().removedByPlayer(FakeWorld.getFakeWorld(block), player, block.getPosition().x,
                    block.getPosition().y, block.getPosition().z, true);
            // TODO: Handle block removal while moving
            // NetworkHandler.instance().sendToAllAround(new PacketSingleBlockSync(block.getStructure().getMotor(), block), world, 64);
            return result;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {

    }

    private boolean drawingHighlight = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawHighlight(DrawBlockHighlightEvent event) {

        if (event == null || event.context == null || event.player == null || event.target == null
                || event.target.typeOfHit != MovingObjectType.BLOCK)
            return;

        if (drawingHighlight)
            return;

        drawingHighlight = true;

        try {
            TileMoving te = get(event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ);
            if (te != null) {
                MovingObjectPosition mop = te.rayTrace(event.player);

                if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                    DrawBlockHighlightEvent ev = new DrawBlockHighlightEvent(event.context, event.player, mop, mop.subHit,
                            event.currentItem, event.partialTicks);
                    World world = event.player.worldObj;

                    MovingStructure s = te.structure;
                    World w = FakeWorld.getFakeWorld(s);

                    if (w != null && s != null) {
                        event.player.worldObj = w;

                        GL11.glPushMatrix();
                        {
                            s.getTrajectory().transformGL(
                                    new Vector3(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY,
                                            TileEntityRendererDispatcher.staticPlayerZ), s.getBlocks(),
                                    s.getTicksMoved() + event.partialTicks);

                            MinecraftForge.EVENT_BUS.post(ev);
                            if (!ev.isCanceled()) {
                                AxisAlignedBB bb = te.getSelectedBoundingBox().copy().offset(-te.xCoord, -te.yCoord, -te.zCoord);
                                setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY,
                                        (float) bb.maxZ);
                                event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
                                setBlockBounds(0, 0, 0, 0, 0, 0);
                            }
                        }
                        GL11.glPopMatrix();

                        event.setCanceled(true);

                        event.player.worldObj = world;
                    }
                }
            }
        } catch (Exception ex) {
            event.setCanceled(false);
        }

        drawingHighlight = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World world, MovingObjectPosition hit, EffectRenderer effectRenderer) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {

        return true;
    }

}
