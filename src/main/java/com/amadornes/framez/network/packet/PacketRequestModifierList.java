package com.amadornes.framez.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.Packet;

public class PacketRequestModifierList extends Packet<PacketRequestModifierList> {

    @Override
    public void handleClientSide(PacketRequestModifierList message, EntityPlayer player) {

        System.out.println("Received request!");
        NetworkHandler.sendToServer(new PacketModifierList());
    }

    @Override
    public void handleServerSide(PacketRequestModifierList message, EntityPlayer player) {

    }

    @Override
    public void read(NBTTagCompound tag) {

    }

    @Override
    public void write(NBTTagCompound tag) {

    }

}
