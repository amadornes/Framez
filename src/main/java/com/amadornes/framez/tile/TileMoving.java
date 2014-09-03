package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import codechicken.lib.raytracer.RayTracer;

import com.amadornes.framez.movement.MovingBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileMoving extends TileEntity {

    private MovingBlock blockA;
    private MovingBlock blockB;

    @Override
    public void updateEntity() {

        if (blockA == null && blockB == null) {
            getWorldObj().removeTileEntity(xCoord, yCoord, zCoord);
            getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    public void setBlockA(MovingBlock blockA) {

        this.blockA = blockA;
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
            blockA.getBlock().addCollisionBoxesToList(blockA.getWorldWrapper(), blockA.getLocation().x, blockA.getLocation().y,
                    blockA.getLocation().z, aabb, lA, e);
            for (Object o : lA) {
                AxisAlignedBB b = ((AxisAlignedBB) o).copy();
                b.minX += blockA.getDirection().offsetX * blockA.getMoved();
                b.minY += blockA.getDirection().offsetY * blockA.getMoved();
                b.minZ += blockA.getDirection().offsetZ * blockA.getMoved();
                b.maxX += blockA.getDirection().offsetX * blockA.getMoved();
                b.maxY += blockA.getDirection().offsetY * blockA.getMoved();
                b.maxZ += blockA.getDirection().offsetZ * blockA.getMoved();

                if (aabb.intersectsWith(b))
                    l.add(b);
            }
        }

        if (blockB != null) {
            AxisAlignedBB aabb2 = aabb.copy();
            aabb2.minX -= blockB.getDirection().offsetX;
            aabb2.minY -= blockB.getDirection().offsetY;
            aabb2.minZ -= blockB.getDirection().offsetZ;
            aabb2.maxX -= blockB.getDirection().offsetX;
            aabb2.maxY -= blockB.getDirection().offsetY;
            aabb2.maxZ -= blockB.getDirection().offsetZ;

            List lB = new ArrayList();
            blockB.getBlock().addCollisionBoxesToList(blockB.getWorldWrapper(), blockB.getLocation().x, blockB.getLocation().y,
                    blockB.getLocation().z, aabb2, lB, e);
            for (Object o : lB) {
                AxisAlignedBB b = ((AxisAlignedBB) o).copy();
                b.minX -= (-blockB.getMoved() * blockB.getDirection().offsetX);
                b.minY -= (-blockB.getMoved() * blockB.getDirection().offsetY);
                b.minZ -= (-blockB.getMoved() * blockB.getDirection().offsetZ);
                b.maxX -= (-blockB.getMoved() * blockB.getDirection().offsetX);
                b.maxY -= (-blockB.getMoved() * blockB.getDirection().offsetY);
                b.maxZ -= (-blockB.getMoved() * blockB.getDirection().offsetZ);

                if (aabb.intersectsWith(b))
                    l.add(b);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox() {

        if (blockA != null) {
            MovingBlock blockA = this.blockA;

            if (blockA.getTileEntity() != null)
                blockA.getTileEntity().setWorldObj(worldObj);

            World w = Minecraft.getMinecraft().thePlayer.worldObj;
            Minecraft.getMinecraft().thePlayer.worldObj = blockA.getWorldWrapper();
            AxisAlignedBB b = blockA.getBlock().getSelectedBoundingBoxFromPool(blockA.getWorldWrapper(), blockA.getLocation().x,
                    blockA.getLocation().y, blockA.getLocation().z);
            Minecraft.getMinecraft().thePlayer.worldObj = w;

            if (b != null) {
                b = b.copy();

                b.minX += blockA.getDirection().offsetX * blockA.getMoved();
                b.minY += blockA.getDirection().offsetY * blockA.getMoved();
                b.minZ += blockA.getDirection().offsetZ * blockA.getMoved();
                b.maxX += blockA.getDirection().offsetX * blockA.getMoved();
                b.maxY += blockA.getDirection().offsetY * blockA.getMoved();
                b.maxZ += blockA.getDirection().offsetZ * blockA.getMoved();

                if (blockA.getTileEntity() != null)
                    blockA.getTileEntity().setWorldObj(blockA.getWorldWrapper());

                return b;
            }

            if (blockA.getTileEntity() != null)
                blockA.getTileEntity().setWorldObj(blockA.getWorldWrapper());
        }

        if (blockB != null) {
            MovingBlock blockB = this.blockB;

            if (blockB.getTileEntity() != null)
                blockB.getTileEntity().setWorldObj(worldObj);

            World w = Minecraft.getMinecraft().thePlayer.worldObj;
            Minecraft.getMinecraft().thePlayer.worldObj = blockB.getWorldWrapper();
            AxisAlignedBB b = blockB.getBlock().getSelectedBoundingBoxFromPool(blockB.getWorldWrapper(), blockB.getLocation().x,
                    blockB.getLocation().y, blockB.getLocation().z);
            Minecraft.getMinecraft().thePlayer.worldObj = w;

            if (b != null) {
                b = b.copy();

                b.minX += blockB.getDirection().offsetX * blockB.getMoved();
                b.minY += blockB.getDirection().offsetY * blockB.getMoved();
                b.minZ += blockB.getDirection().offsetZ * blockB.getMoved();
                b.maxX += blockB.getDirection().offsetX * blockB.getMoved();
                b.maxY += blockB.getDirection().offsetY * blockB.getMoved();
                b.maxZ += blockB.getDirection().offsetZ * blockB.getMoved();

                if (blockB.getTileEntity() != null)
                    blockB.getTileEntity().setWorldObj(blockB.getWorldWrapper());

                return b;
            }

            if (blockB.getTileEntity() != null)
                blockB.getTileEntity().setWorldObj(blockB.getWorldWrapper());
        }

        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public MovingObjectPosition rayTrace(Vec3 start, Vec3 end) {

        MovingObjectPosition mopA = null;
        MovingObjectPosition mopB = null;

        if (blockA != null) {
            MovingBlock blockA = this.blockA;
            Vec3 start2 = start.addVector(blockA.getDirection().offsetX * (-blockA.getMoved()), blockA.getDirection().offsetY * (-blockA.getMoved()),
                    blockA.getDirection().offsetZ * (-blockA.getMoved()));
            Vec3 end2 = end.addVector(blockA.getDirection().offsetX * (-blockA.getMoved()), blockA.getDirection().offsetY * (-blockA.getMoved()),
                    blockA.getDirection().offsetZ * (-blockA.getMoved()));

            mopA = blockA.getBlock().collisionRayTrace(blockA.getWorldWrapper(), blockA.getLocation().x, blockA.getLocation().y,
                    blockA.getLocation().z, start2, end2);
            if (mopA != null)
                mopA.hitInfo = blockA;
        }

        if (blockB != null) {
            MovingBlock blockB = this.blockB;
            Vec3 start2 = start.addVector(blockB.getDirection().offsetX * (-blockB.getMoved()), blockB.getDirection().offsetY * (-blockB.getMoved()),
                    blockB.getDirection().offsetZ * (-blockB.getMoved()));
            Vec3 end2 = end.addVector(blockB.getDirection().offsetX * (-blockB.getMoved()), blockB.getDirection().offsetY * (-blockB.getMoved()),
                    blockB.getDirection().offsetZ * (-blockB.getMoved()));

            mopB = blockB.getBlock().collisionRayTrace(blockB.getWorldWrapper(), blockB.getLocation().x, blockB.getLocation().y,
                    blockB.getLocation().z, start2, end2);
            if (mopB != null)
                mopB.hitInfo = blockB;
        }

        if (mopA == null && mopB == null)
            return null;
        if (mopA != null && mopB == null)
            return mopA;
        if (mopB != null && mopA == null)
            return mopB;

        if (mopA.hitVec.distanceTo(start) < mopB.hitVec.distanceTo(start))
            return mopA;
        return mopB;
    }

    public MovingObjectPosition rayTrace(EntityPlayer player) {

        return rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
    }

    public MovingBlock getSelected(MovingObjectPosition mop) {

        if (mop == null)
            return null;

        if (mop.hitInfo != null && mop.hitInfo instanceof MovingBlock)
            return (MovingBlock) mop.hitInfo;

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

        return b.getBlock().onBlockActivated(b.getWorldWrapper(), b.getLocation().x, b.getLocation().y, b.getLocation().z, player, mop.sideHit,
                (float) mop.hitVec.xCoord - mop.blockX, (float) mop.hitVec.yCoord - mop.blockY, (float) mop.hitVec.zCoord - mop.blockZ);
    }
}
