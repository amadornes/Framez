package com.amadornes.framez.network;

import com.amadornes.framez.Framez;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class PacketShowGUI extends Packet<PacketShowGUI> {

    private int gui;
    private BlockPos pos;

    public PacketShowGUI(int gui, BlockPos pos) {

        this.gui = gui;
        this.pos = pos;
    }

    public PacketShowGUI() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        player.openGui(Framez.instance, gui, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        player.openGui(Framez.instance, gui, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void toBytes(PacketBuffer buf) {

        buf.writeInt(gui);
        buf.writeBlockPos(pos);
    }

    @Override
    public void fromBytes(PacketBuffer buf) {

        gui = buf.readInt();
        pos = buf.readBlockPos();
    }

}
