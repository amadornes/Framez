package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import codechicken.lib.raytracer.RayTracer;

import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.movement.MovingBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileMoving extends TileEntity {

    private MovingBlock blockA;
    private MovingBlock blockB;

    private UpdateType type;

    private boolean needsUpdate = false;;

    @Override
    public void updateEntity() {

        if (blockA != null) {
            if (blockA.getTileEntity() != null)
                blockA.getTileEntity().updateEntity();
            if (blockA.getMoved() >= 1)
                blockA = null;
        }
        if (blockB != null)
            if (blockB.getMoved() >= 1)
                blockB = null;

        if (blockA == null && blockB == null) {
            if (worldObj.getBlock(xCoord, yCoord, zCoord) == FramezBlocks.block_moving)
                worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
            invalidate();
        }

        if (needsUpdate) {
            sendUpdatePacket(UpdateType.ALL);
            needsUpdate = false;
        }
    }

    public void setBlockA(MovingBlock blockA) {

        this.blockA = blockA;

        needsUpdate = true;
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

    public int getLightValue() {

        // double a = 0;
        // double b = 0;
        //
        // // if (blockA != null) {
        // // a = blockA.getBlock().getLightValue(blockA.getWorldWrapper(), blockA.getLocation().x, blockA.getLocation().y, blockA.getLocation().z)
        // // * (1 - blockA.getMoved());
        // // }
        // if (blockB != null) {
        // b = blockB.getBlock().getLightValue(blockB.getWorldWrapper(), blockB.getLocation().x, blockB.getLocation().y, blockB.getLocation().z)
        // * blockB.getMoved();
        // System.out.println(b);
        // }

        // return (int) Math.min(Math.max(b, 0), 15);

        if (blockA != null)
            return blockA.getBlock().getLightValue(blockA.getWorldWrapper(), blockA.getLocation().x, blockA.getLocation().y, blockA.getLocation().z);
        if (blockB != null)
            return blockB.getBlock().getLightValue(blockB.getWorldWrapper(), blockB.getLocation().x, blockB.getLocation().y, blockB.getLocation().z);

        return 0;
    }

    public int getLightOpacity() {

        return 0;
    }

    public void randomDisplayTick(Random rnd) {

        if (blockA != null)
            if (blockA.getBlock().getTickRandomly())
                blockA.getBlock().randomDisplayTick(blockA.getWorldWrapper(), blockA.getLocation().x, blockA.getLocation().y, blockA.getLocation().z,
                        rnd);
    }

    public ItemStack getPickBlock(MovingObjectPosition target) {

        MovingObjectPosition mop = rayTrace(Minecraft.getMinecraft().thePlayer);
        if (mop == null)
            return null;
        MovingBlock block = getSelected(mop);
        if (block == null)
            return null;

        return block.getBlock().getPickBlock(mop, block.getWorldWrapper(), block.getX(), block.getY(), block.getZ());
    }

    public void sendUpdatePacket(UpdateType type) {

        this.type = type;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();

        if (blockA != null && type != null) {
            if (type.isBlock()) {
                UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(blockA.getBlock());
                tag.setString("mod", ui.modId);
                tag.setString("block", ui.name);
                tag.setInteger("meta", blockA.getMetadata());
            }
            if (type.isTile() && blockA.getTileEntity() != null) {
                Packet p = blockA.getTileEntity().getDescriptionPacket();
                if (p != null && p instanceof S35PacketUpdateTileEntity)
                    tag.setTag("tile", ((S35PacketUpdateTileEntity) p).func_148857_g());
            }
        }

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        NBTTagCompound tag = pkt.func_148857_g();

        if (!tag.hasKey("block") && !tag.hasKey("tile")) {
            if (blockA != null && blockA.getTileEntity() != null)
                blockA.getTileEntity().onDataPacket(net, pkt);
            return;
        }

        if (blockA == null)
            return;

        if (tag.hasKey("block")) {
            Block bl = GameRegistry.findBlock(tag.getString("mod"), tag.getString("block"));
            if (blockA.getBlock() != bl)
                blockA.setBlock(bl);
            int meta = tag.getInteger("meta");
            if (blockA.getMetadata() != meta)
                blockA.setMetadata(meta);
            blockA.setRenderList(-1);
        }
        if (tag.hasKey("tile") && blockA.getTileEntity() != null) {
            S35PacketUpdateTileEntity p = new S35PacketUpdateTileEntity(blockA.getLocation().x, blockA.getLocation().y, blockA.getLocation().z, 0,
                    tag.getCompoundTag("tile"));
            blockA.getTileEntity().onDataPacket(net, p);
        }
    }

    public static enum UpdateType {
        BLOCK(true, false), TILE(false, true), ALL(true, true);

        private boolean block;
        private boolean tile;

        private UpdateType(boolean block, boolean tile) {

            this.block = block;
            this.tile = tile;
        }

        public boolean isBlock() {

            return block;
        }

        public boolean isTile() {

            return tile;
        }

    }
}
