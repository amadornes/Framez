package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.Framez;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.util.Timing;
import com.amadornes.framez.world.FakeWorld;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileMoving extends TileEntity {

    private MovingBlock blockA = null, blockB = null;

    @Override
    public void updateEntity() {

        if (blockA != null) {
            if (blockA.getTileEntity() != null && blockA.getTileEntity().canUpdate())
                blockA.getTileEntity().updateEntity();
            if (blockA.getStructure().getProgress() >= 1)
                blockA = null;
        }
        if (blockB != null)
            if (blockB.getStructure().getProgress() >= 1)
                blockB = null;

        if (blockA == null && blockB == null) {
            if (worldObj.getBlock(xCoord, yCoord, zCoord) == FramezBlocks.moving) {
                worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air, 0, 0);
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 0);
            }
            invalidate();
        }

        // if (needsUpdate) {
        // sendUpdatePacket(UpdateType.ALL);
        // needsUpdate = false;
        // }
    }

    public void setBlockA(MovingBlock blockA) {

        this.blockA = blockA;

        // needsUpdate = true;
    }

    public void setBlockB(MovingBlock blockB) {

        this.blockB = blockB;
    }

    public MovingBlock getBlockA() {

        return blockA;
    }

    public MovingBlock getBlockB() {

        return blockB;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addCollisionBoxesToList(AxisAlignedBB aabb, List l, Entity e) {

        if (blockA != null) {
            List lA = new ArrayList();
            blockA.getBlock().addCollisionBoxesToList(FakeWorld.getFakeWorld(blockA), blockA.getX(), blockA.getY(), blockA.getZ(), aabb,
                    lA, e);
            for (Object o : lA) {
                AxisAlignedBB b = ((AxisAlignedBB) o).copy();

                Vec3d min = blockA
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.minX, b.minY, b.minZ),
                                blockA.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - 1.25));
                Vec3d max = blockA
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.maxX, b.maxY, b.maxZ),
                                blockA.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - 1.25));

                b.minX = min.getX();
                b.minY = min.getY();
                b.minZ = min.getZ();
                b.maxX = max.getX();
                b.maxY = max.getY();
                b.maxZ = max.getZ();

                if (aabb.intersectsWith(b))
                    l.add(b);
            }
        }

        // FIXME Block "B" collision boxes
        // if (blockB != null) {
        // Vec3d minAABB = blockB
        // .getStructure()
        // .getMovement()
        // .transform(new Vec3d(aabb.minX, aabb.minY, aabb.minZ),
        // blockB.getStructure().getInterpolatedProgress(-Framez.proxy.getFrame()));
        // Vec3d maxAABB = blockB
        // .getStructure()
        // .getMovement()
        // .transform(new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ),
        // blockB.getStructure().getInterpolatedProgress(-Framez.proxy.getFrame()));
        // AxisAlignedBB aabb2 = AxisAlignedBB.getBoundingBox(minAABB.getX(), minAABB.getY(), minAABB.getZ(), maxAABB.getX(),
        // maxAABB.getY(), maxAABB.getZ());
        //
        // List lB = new ArrayList();
        // blockB.getBlock().addCollisionBoxesToList(FakeWorld.getFakeWorld(blockB), blockB.getX(), blockB.getY(), blockB.getZ(), aabb2,
        // lB, e);
        // for (Object o : lB) {
        // AxisAlignedBB b = ((AxisAlignedBB) o).copy();
        //
        // Vec3d min = blockB
        // .getStructure()
        // .getMovement()
        // .transform(new Vec3d(b.minX, b.minY, b.minZ),
        // blockB.getStructure().getInterpolatedProgress(-Framez.proxy.getFrame()));
        // Vec3d max = blockB
        // .getStructure()
        // .getMovement()
        // .transform(new Vec3d(b.maxX, b.maxY, b.maxZ),
        // blockB.getStructure().getInterpolatedProgress(-Framez.proxy.getFrame()));
        //
        // b.minX = min.getX();
        // b.minY = min.getY();
        // b.minZ = min.getZ();
        // b.maxX = max.getX();
        // b.maxY = max.getY();
        // b.maxZ = max.getZ();
        //
        // // if (aabb2.intersectsWith(b))
        // l.add(b);
        // }
        // }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox() {

        if (blockA != null) {
            MovingBlock blockA = this.blockA;

            if (blockA.getTileEntity() != null)
                blockA.getTileEntity().setWorldObj(worldObj);

            World w = Minecraft.getMinecraft().theWorld;
            Framez.proxy.getPlayer().worldObj = FakeWorld.getFakeWorld(blockA);
            AxisAlignedBB b = blockA.getBlock().getSelectedBoundingBoxFromPool(FakeWorld.getFakeWorld(blockA), blockA.getX(),
                    blockA.getY(), blockA.getZ());
            Framez.proxy.getPlayer().worldObj = w;

            if (b != null) {
                b = b.copy();

                Vec3d min = blockA
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.minX, b.minY, b.minZ),
                                blockA.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))));
                Vec3d max = blockA
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.maxX, b.maxY, b.maxZ),
                                blockA.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))));

                b.minX = min.getX();
                b.minY = min.getY();
                b.minZ = min.getZ();
                b.maxX = max.getX();
                b.maxY = max.getY();
                b.maxZ = max.getZ();

                if (blockA.getTileEntity() != null)
                    blockA.getTileEntity().setWorldObj(FakeWorld.getFakeWorld(blockA));

                return b;
            }

            if (blockA.getTileEntity() != null)
                blockA.getTileEntity().setWorldObj(FakeWorld.getFakeWorld(blockA));
        }

        if (blockB != null) {
            MovingBlock blockB = this.blockB;

            if (blockB.getTileEntity() != null)
                blockB.getTileEntity().setWorldObj(worldObj);

            World w = Minecraft.getMinecraft().theWorld;
            Framez.proxy.getPlayer().worldObj = FakeWorld.getFakeWorld(blockB);
            AxisAlignedBB b = blockB.getBlock().getSelectedBoundingBoxFromPool(FakeWorld.getFakeWorld(blockB), blockB.getX(),
                    blockB.getY(), blockB.getZ());
            Framez.proxy.getPlayer().worldObj = w;

            if (b != null) {
                b = b.copy();

                Vec3d min = blockB
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.minX, b.minY, b.minZ),
                                blockB.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))));
                Vec3d max = blockB
                        .getStructure()
                        .getMovement()
                        .transform(new Vec3d(b.maxX, b.maxY, b.maxZ),
                                blockB.getStructure().getInterpolatedProgress(Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))));

                b.minX = min.getX();
                b.minY = min.getY();
                b.minZ = min.getZ();
                b.maxX = max.getX();
                b.maxY = max.getY();
                b.maxZ = max.getZ();

                if (blockB.getTileEntity() != null)
                    blockB.getTileEntity().setWorldObj(FakeWorld.getFakeWorld(blockB));

                return b;
            }

            if (blockB.getTileEntity() != null)
                blockB.getTileEntity().setWorldObj(FakeWorld.getFakeWorld(blockB));
        }

        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public MovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        MovingObjectPosition mopA = null;
        MovingObjectPosition mopB = null;

        Vec3d start2 = null;
        Vec3d end2 = null;

        if (blockA != null) {
            start2 = blockA
                    .getStructure()
                    .getMovement()
                    .transform(
                            start,
                            -(worldObj.isRemote ? blockA.getStructure().getInterpolatedProgress(
                                    Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))) : blockA.getStructure().getProgress()));
            end2 = blockA
                    .getStructure()
                    .getMovement()
                    .transform(
                            end,
                            -(worldObj.isRemote ? blockA.getStructure().getInterpolatedProgress(
                                    Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))) : blockA.getStructure().getProgress()));

            mopA = blockA.getBlock().collisionRayTrace(FakeWorld.getFakeWorld(blockA), blockA.getX(), blockA.getY(), blockA.getZ(),
                    start2.toVec3(), end2.toVec3());
            if (mopA != null)
                mopA.hitInfo = new Pair<MovingBlock, Object>(blockA, mopA.hitInfo);
        }

        if (blockB != null) {
            if (start2 == null) {
                start2 = blockB
                        .getStructure()
                        .getMovement()
                        .transform(
                                start,
                                -(worldObj.isRemote ? blockB.getStructure().getInterpolatedProgress(
                                        Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))) : blockB.getStructure().getProgress()));
                end2 = blockB
                        .getStructure()
                        .getMovement()
                        .transform(
                                end,
                                -(worldObj.isRemote ? blockB.getStructure().getInterpolatedProgress(
                                        Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))) : blockB.getStructure().getProgress()));
            }

            mopB = blockB.getBlock().collisionRayTrace(FakeWorld.getFakeWorld(blockB), blockB.getX(), blockB.getY(), blockB.getZ(),
                    start2.toVec3(), end2.toVec3());
            if (mopB != null) {
                mopB.hitInfo = new Pair<MovingBlock, Object>(blockB, mopB.hitInfo);
                Vec3i v = blockB.getStructure().getMovement().transform(new Vec3i(mopB.blockX, mopB.blockY, mopB.blockZ));
                mopB.blockX = v.getX();
                mopB.blockY = v.getY();
                mopB.blockZ = v.getZ();
            }
        }

        if (mopA == null && mopB == null)
            return null;
        if (mopA != null && mopB == null)
            return mopA;
        if (mopB != null && mopA == null)
            return mopB;

        if (mopA.hitVec.squareDistanceTo(start2.toVec3()) < mopB.hitVec.squareDistanceTo(start2.toVec3()))
            return mopA;
        return mopB;
    }

    public MovingObjectPosition rayTrace(EntityPlayer player) {

        return rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
    }

    public MovingBlock getSelected(MovingObjectPosition mop) {

        if (mop == null)
            return null;

        if (mop.hitInfo != null && mop.hitInfo instanceof Entry<?, ?> && ((Entry<?, ?>) mop.hitInfo).getKey() != null
                && ((Entry<?, ?>) mop.hitInfo).getKey() instanceof MovingBlock)
            return (MovingBlock) ((Entry<?, ?>) mop.hitInfo).getKey();

        return null;
    }

    public MovingBlock getSelected(EntityPlayer player) {

        return getSelected(rayTrace(player));
    }

    public boolean onBlockActivated(EntityPlayer player) {

        MovingObjectPosition mop = rayTrace(player);
        MovingBlock b = getSelected(mop);

        if (b == null)
            return false;

        boolean result = b.getBlock().onBlockActivated(FakeWorld.getFakeWorld(b), b.getX(), b.getY(), b.getZ(), player, mop.sideHit,
                (float) mop.hitVec.xCoord - mop.blockX, (float) mop.hitVec.yCoord - mop.blockY, (float) mop.hitVec.zCoord - mop.blockZ);

        return result;
    }

    public int getLightValue() {

        if (blockA != null)
            return blockA.getBlock().getLightValue(FakeWorld.getFakeWorld(blockA), blockA.getX(), blockA.getY(), blockA.getZ());

        return 0;
    }

    public int getLightOpacity() {

        return 0;
    }

    public void randomDisplayTick(Random rnd) {

        if (blockA != null) {
            blockA.getBlock().randomDisplayTick(FakeWorld.getFakeWorld(blockA), blockA.getX(), blockA.getY(), blockA.getZ(), rnd);
        }
    }

    public ItemStack getPickBlock(MovingObjectPosition target, EntityPlayer player) {

        MovingObjectPosition mop = rayTrace(Framez.proxy.getPlayer());
        if (mop == null)
            return null;
        MovingBlock block = getSelected(mop);
        if (block == null)
            return null;

        return block.getBlock().getPickBlock(mop, FakeWorld.getFakeWorld(block), block.getX(), block.getY(), block.getZ(), player);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        // if (blockA != null) {
        // TileEntity te = blockA.getTileEntity();
        // if (te != null)
        // return te
        // .getRenderBoundingBox()
        // .copy().expand(1, 1, 1);
        // return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).offset(
        // blockA.getDirection().offsetX * blockA.getMoved(), blockA.getDirection().offsetY * blockA.getMoved(),
        // blockA.getDirection().offsetZ * blockA.getMoved());
        // }

        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(1, 1, 1);
    }

}
