package com.amadornes.framez.network;

import java.io.IOException;

import com.amadornes.framez.tile.TileMotor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

public class PacketUpdateTrigger extends LocatedPacket<PacketUpdateTrigger> {

    private int action;
    private NBTTagCompound tag;

    public PacketUpdateTrigger(TileMotor motor, int action) {

        super(motor);
        this.action = action;
        this.tag = motor.writeTriggerToNBT(motor.triggers.get(motor.actionIdMap.get(action)));
    }

    public PacketUpdateTrigger() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(pos);
        if (tile != null && tile instanceof TileMotor) {
            TileMotor te = (TileMotor) tile;
            te.readTriggerFromNBT(te.triggers.get(te.actionIdMap.get(action)), tag);
        }
    }

    @Override
    public void toBytes(PacketBuffer buf) {

        buf.writeInt(action);
        buf.writeNBTTagCompoundToBuffer(tag);
    }

    @Override
    public void fromBytes(PacketBuffer buf) {

        action = buf.readInt();
        try {
            tag = buf.readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
