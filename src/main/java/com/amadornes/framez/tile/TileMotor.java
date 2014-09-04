package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.api.movement.IFrameMove;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketStartMoving;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.util.Utils;

public abstract class TileMotor extends TileEntity implements IFrameMove {

    public abstract boolean canMove();

    public abstract double getMovementSpeed();

    public Object getExtraInfo() {

        return null;
    }

    private ForgeDirection face = ForgeDirection.DOWN;

    private ForgeDirection direction = ForgeDirection.SOUTH;

    private MovingStructure structure = null;

    public ForgeDirection getFace() {

        return face;
    }

    public ForgeDirection getDirection() {

        return direction;
    }

    public void setFace(ForgeDirection face) {

        this.face = face;

        sendUpdatePacket();
    }

    public void setDirection(ForgeDirection direction) {

        this.direction = direction;

        sendUpdatePacket();
    }

    public double getMoved() {

        return structure == null ? 0 : structure.getMoved();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        writeUpdatePacket(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        readUpdatePacket(tag);
    }

    public void writeUpdatePacket(NBTTagCompound tag) {

        tag.setInteger("face", getFace().ordinal());
        tag.setInteger("direction", getDirection().ordinal());
    }

    public void readUpdatePacket(NBTTagCompound tag) {

        face = ForgeDirection.getOrientation(tag.getInteger("face"));
        direction = ForgeDirection.getOrientation(tag.getInteger("direction"));
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();
        writeUpdatePacket(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readUpdatePacket(pkt.func_148857_g());
    }

    public void sendUpdatePacket() {

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!worldObj.isRemote) {
            if (canMove() && worldObj.getBlock(xCoord + face.offsetX, yCoord + face.offsetY, zCoord + face.offsetZ) != Blocks.air
                    && structure == null) {
                structure = new MovingStructure(worldObj, direction, getMovementSpeed() / 100D);

                PartFrame frame = Utils.getFrame(worldObj, xCoord + face.offsetX, yCoord + face.offsetY, zCoord + face.offsetZ);
                if (frame != null) {
                    List<BlockCoord> blocks = new ArrayList<BlockCoord>();
                    Utils.addConnected(blocks, frame);
                    blocks.remove(new BlockCoord(xCoord, yCoord, zCoord));
                    structure.addBlocks(blocks);
                    blocks.clear();
                } else {
                    structure.addBlock(xCoord + face.offsetX, yCoord + face.offsetY, zCoord + face.offsetZ);
                }

                StructureTickHandler.INST.addStructure(structure);

                NetworkHandler.sendToDimension(new PacketStartMoving(this), worldObj.provider.dimensionId);
            }
        }
        if (structure != null && structure.getMoved() >= 1)
            structure = null;
    }

    public MovingStructure getStructure() {

        return structure;
    }

    public void setStructure(MovingStructure structure) {

        this.structure = structure;
    }

    public void randomDisplayTick(Random rnd) {

        if (structure != null)
            for (MovingBlock b : structure.getBlocks())
                if (b != null && b.getBlock() != null)
                    if (b.getBlock().getTickRandomly())
                        b.getBlock().randomDisplayTick(structure.getWorldWrapper(), b.getLocation().x, b.getLocation().y, b.getLocation().z, rnd);
    }

    @Override
    public boolean canBeMoved() {

        return structure == null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        if (structure != null) {
            int x1 = xCoord, y1 = yCoord, z1 = zCoord, x2 = xCoord, y2 = yCoord, z2 = zCoord;
            for (MovingBlock b : new ArrayList<MovingBlock>(structure.getBlocks())) {
                x1 = Math.min(x1, b.getLocation().x);
                y1 = Math.min(y1, b.getLocation().y);
                z1 = Math.min(z1, b.getLocation().z);
                x2 = Math.max(x2, b.getLocation().x);
                y2 = Math.max(y2, b.getLocation().y);
                z2 = Math.max(z2, b.getLocation().z);
            }
            x1 -= getDirection().offsetX < 0 ? 1 : 0;
            y1 -= getDirection().offsetY < 0 ? 1 : 0;
            z1 -= getDirection().offsetZ < 0 ? 1 : 0;
            x2 += getDirection().offsetX > 0 ? 1 : 0;
            y2 += getDirection().offsetY > 0 ? 1 : 0;
            z2 += getDirection().offsetZ > 0 ? 1 : 0;

            return AxisAlignedBB.getBoundingBox(x1, y1, z1, x2, y2, z2);
        }

        return super.getRenderBoundingBox();
    }

}
