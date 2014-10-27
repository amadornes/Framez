package com.amadornes.framez.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.network.LocatedPacket;

public class PacketBlockSide extends LocatedPacket<PacketBlockSide> {

    private ForgeDirection side;

    public PacketBlockSide(int x, int y, int z, ForgeDirection side) {

        super(x, y, z);

        this.side = side;
    }

    public PacketBlockSide() {

    }

    @Override
    public void handleClientSide(PacketBlockSide message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(PacketBlockSide message, EntityPlayer player) {

        IFrame frame = FramezApi.inst().getFrame(player.worldObj, x, y, z);
        if (frame == null)
            return;

        frame.toggleBlock(side);
    }

    @Override
    public void write(NBTTagCompound tag) {

        super.write(tag);
        tag.setInteger("side", side.ordinal());
    }

    @Override
    public void read(NBTTagCompound tag) {

        super.read(tag);
        side = ForgeDirection.getOrientation(tag.getInteger("side"));
    }
}
