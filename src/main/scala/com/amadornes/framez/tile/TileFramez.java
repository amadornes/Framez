package com.amadornes.framez.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileFramez extends TileEntity {

    public World getWorld() {

        return getWorldObj();
    }

    public int getX() {

        return xCoord;
    }

    public int getY() {

        return yCoord;
    }

    public int getZ() {

        return zCoord;
    }

    public void writeUpdateToNBT(NBTTagCompound tag) {

    }

    public void readUpdateFromNBT(NBTTagCompound tag) {

    }

    public void sendUpdate() {

        if (getWorld() != null)
            getWorld().markBlockForUpdate(getX(), getY(), getZ());
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();
        writeUpdateToNBT(tag);
        return new S35PacketUpdateTileEntity(getX(), getY(), getZ(), 2, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readUpdateFromNBT(pkt.func_148857_g());
    }

    public void markRender() {

        if (getWorld() != null)
            getWorld().func_147479_m(getX(), getY(), getZ());
    }

    public void notifyNeighbours() {

        if (getWorld() != null && !getWorld().isRemote)
            getWorld().notifyBlockChange(getX(), getY(), getZ(), getBlockType());
    }

    public void notifyChange() {

        markDirty();
        markRender();
        notifyNeighbours();
        sendUpdate();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        saveToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        loadFromNBT(tag);
    }

    public void saveToNBT(NBTTagCompound tag) {

    }

    public void loadFromNBT(NBTTagCompound tag) {

    }

}
