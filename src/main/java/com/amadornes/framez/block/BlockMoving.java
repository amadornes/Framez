package com.amadornes.framez.block;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.network.PacketSingleBlockSync;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMoving extends BlockContainer implements IMovable {

    public BlockMoving() {

        super(Material.rock);

        setBlockName(References.Block.MOVING);

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

        MovingObjectPosition mop = te.rayTrace(new Vec3d(start), new Vec3d(end));
        if (mop != null)
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

        return te.onBlockActivated(p);
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rnd) {

        TileMoving te = get(w, x, y, z);
        if (te == null)
            return;
        if (te.getBlockA() == null)
            return;
        te.getBlockA().getBlock().updateTick(FakeWorld.getFakeWorld(te.getBlockA()), x, y, z, rnd);
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {

        TileMoving te = get(world, x, y, z);
        if (te == null)
            return false;

        MovingObjectPosition mop = te.rayTrace(player);
        if (mop != null) {
            MovingBlock b = (MovingBlock) ((Entry<?, ?>) mop.hitInfo).getKey();
            boolean result = b.getBlock().removedByPlayer(FakeWorld.getFakeWorld(b), player, b.getX(), b.getY(), b.getZ());
            com.amadornes.framez.network.NetworkHandler.instance().sendToAllAround(
                    new PacketSingleBlockSync(b.getStructure().getMotor(), b), world, 64);
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

        if (event == null || event.context == null || event.currentItem == null || event.player == null || event.target == null
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
                    World w = null;
                    MovingBlock a = te.getBlockA();
                    MovingBlock b = te.getBlockB();

                    Vec3i pos = null;
                    MovingStructure structure = null;
                    if (a != null) {
                        w = a.getRenderList() > 0 ? FakeWorld.getFakeWorld(a) : null;
                        pos = new Vec3i(a);
                        structure = a.getStructure();
                    } else if (b != null) {
                        w = b.getRenderList() > 0 ? FakeWorld.getFakeWorld(b) : null;
                        pos = new Vec3i(a);
                        structure = b.getStructure();
                    }

                    if (w != null) {
                        event.player.worldObj = w;

                        GL11.glPushMatrix();
                        {
                            GL11.glTranslated(-pos.getX(), -pos.getY(), -pos.getZ());
                            Vec3d t = structure.getMovement().transform(new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
                                    structure.getInterpolatedProgress(Framez.proxy.getFrame() - 1));
                            GL11.glTranslated(t.getX(), t.getY(), t.getZ());
                            MinecraftForge.EVENT_BUS.post(ev);
                        }
                        GL11.glPopMatrix();

                        if (ev.isCanceled())
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
    @Priority(PriorityEnum.OVERRIDE)
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        return BlockMovementType.UNMOVABLE;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int face) {

        TileMoving te = get(world, x, y, z);
        if (te == null)
            return null;

        if (te.getBlockA() != null)
            return te
                    .getBlockA()
                    .getBlock()
                    .getIcon(FakeWorld.getFakeWorld(te.getBlockA()), te.getBlockA().getX(), te.getBlockA().getY(), te.getBlockA().getZ(),
                            face);
        if (te.getBlockB() != null)
            return te
                    .getBlockB()
                    .getBlock()
                    .getIcon(FakeWorld.getFakeWorld(te.getBlockB()), te.getBlockB().getX(), te.getBlockB().getY(), te.getBlockB().getZ(),
                            face);

        return null;
    }
}
