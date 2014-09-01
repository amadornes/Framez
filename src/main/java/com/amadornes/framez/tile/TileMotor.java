package com.amadornes.framez.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.util.Utils;

public abstract class TileMotor extends TileEntity {

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

        if (canMove() && worldObj.getBlock(xCoord + face.offsetX, yCoord + face.offsetY, zCoord + face.offsetZ) != Blocks.air && structure == null) {
            structure = new MovingStructure(worldObj, direction, getMovementSpeed() / 64D);

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

            StructureTickHandler.inst.addStructure(structure);
        }
        if (structure != null && !structure.isValid())
            structure = null;
    }

}
