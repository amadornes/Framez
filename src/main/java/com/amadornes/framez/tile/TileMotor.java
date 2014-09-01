package com.amadornes.framez.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileMotor extends TileEntity {

    public abstract boolean canMove();

    public abstract double getMovementSpeed();

    public Object getExtraInfo() {

        return null;
    }

    private ForgeDirection face = ForgeDirection.DOWN;

    private ForgeDirection direction = ForgeDirection.SOUTH;

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

}
