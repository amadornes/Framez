package com.amadornes.trajectory.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

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

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.amadornes.trajectory.Trajectory;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.client.RenderMoving;
import com.amadornes.trajectory.movement.MovementManager;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.util.RayTracingUtils;
import com.amadornes.trajectory.world.FakeWorld;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileMoving extends TileEntity {

    public MovingStructure structure;

    public TileMoving(MovingStructure structure) {

        this.structure = structure;
    }

    public TileMoving() {

    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (structure == null || structure.getTrajectory().getProgress(structure.getTicksMoved()) >= 1) {
            if (!getWorldObj().isRemote) {
                getWorldObj().removeTileEntity(xCoord, yCoord, zCoord);
                getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
            }
        } else {
            FakeWorld.getFakeWorld(structure);
            MovingBlock b = (MovingBlock) structure.getBlocks().find(xCoord, yCoord, zCoord);
            if (b != null)
                b.tick();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addCollisionBoxesToList(AxisAlignedBB aabb, List l, Entity e) {

        if (structure == null)
            return;

        World fakeWorld = FakeWorld.getFakeWorld(structure);
        int ticks = structure.getTicksMoved();
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        for (IMovingBlock b : structure.getBlocks()) {
            b.getBlock().addCollisionBoxesToList(fakeWorld, b.getPosition().x, b.getPosition().y, b.getPosition().z,
                    TileEntity.INFINITE_EXTENT_AABB, boxes, e);
            for (AxisAlignedBB bb : boxes)
                for (AxisAlignedBB box : structure.getTrajectory().transformAABB(bb, ticks))
                    if (box.intersectsWith(aabb))
                        l.add(box);
            boxes.clear();
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox() {

        MovingObjectPosition mop = rayTrace(Trajectory.proxy.getPlayer());
        if (mop != null)
            return structure.getBlocks().find(mop.blockX, mop.blockY, mop.blockZ).getBlock()
                    .getSelectedBoundingBoxFromPool(FakeWorld.getFakeWorld(structure), mop.blockX, mop.blockY, mop.blockZ);
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    public MovingObjectPosition rayTrace(Vector3 start, Vector3 end) {

        if (structure == null)
            return null;

        World w = FakeWorld.getFakeWorld(structure);

        float ticks = -structure.getTicksMoved() - 1;

        start = structure.getTrajectory().transformVec(start, ticks);
        end = structure.getTrajectory().transformVec(end, ticks);

        MovingObjectPosition mop = w.rayTraceBlocks(Vec3.createVectorHelper(start.x, start.y, start.z),
                Vec3.createVectorHelper(end.x, end.y, end.z));
        if (mop != null)
            mop.hitInfo = new ImmutablePair<MovingBlock, Object>((MovingBlock) structure.getBlocks().find(mop.blockX, mop.blockY,
                    mop.blockZ), mop.hitInfo);
        return mop;
    }

    public MovingObjectPosition rayTrace(EntityPlayer player) {

        return rayTrace(new Vector3(RayTracingUtils.getStartVector(player)), new Vector3(RayTracingUtils.getEndVector(player)));
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
        MovingBlock block = getSelected(mop);

        if (block == null)
            return false;

        boolean result = block.getBlock().onBlockActivated(FakeWorld.getFakeWorld(block), block.getPosition().x, block.getPosition().y,
                block.getPosition().z, player, mop.sideHit, (float) mop.hitVec.xCoord - mop.blockX, (float) mop.hitVec.yCoord - mop.blockY,
                (float) mop.hitVec.zCoord - mop.blockZ);

        return result;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, EntityPlayer player) {

        MovingObjectPosition mop = rayTrace(Trajectory.proxy.getPlayer());
        if (mop == null)
            return new ItemStack(Blocks.stone);
        MovingBlock block = getSelected(mop);
        if (block == null)
            return new ItemStack(Blocks.planks);

        return block.getBlock().getPickBlock(mop, FakeWorld.getFakeWorld(block), block.getPosition().x, block.getPosition().y,
                block.getPosition().z, player);
    }

    public int getLightValue() {

        IMovingBlock b = structure == null ? null : structure.getBlocks().find(xCoord, yCoord, zCoord);
        if (b == null)
            return 0;
        return b.getBlock().getLightValue(b.getFakeWorld(), xCoord, yCoord, zCoord);
    }

    public void randomDisplayTick(Random rnd) {

        IMovingBlock b = structure == null ? null : structure.getBlocks().find(xCoord, yCoord, zCoord);
        if (b == null)
            return;
        b.getBlock().randomDisplayTick(b.getFakeWorld(), xCoord, yCoord, zCoord, rnd);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {

        return Integer.MAX_VALUE;
    }

    protected void writeToPacketNBT(NBTTagCompound tag) {

        if (structure != null)
            tag.setString("structure", structure.getId().toString());
    }

    protected void readFromPacketNBT(NBTTagCompound tag) {

        if (tag.hasKey("structure")) {
            structure = MovementManager.instance.findStructure(true, UUID.fromString(tag.getString("structure")));
            if (structure != null)
                structure.registerPlaceholder(this);
        } else {
            structure = null;
        }
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tCompound = new NBTTagCompound();
        writeToPacketNBT(tCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readFromPacketNBT(pkt.func_148857_g());
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();

        if (structure != null && structure.getTrajectory().getProgress(structure.getTicksMoved()) < 1)
            structure.finishMoving();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderInPass(int pass) {

        RenderMoving.pass = pass;
        return true;
    }

}
