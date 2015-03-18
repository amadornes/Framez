package com.amadornes.framez.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.network.Packet;

import com.amadornes.framez.item.ItemWrench;

public class PacketWrenchMode extends Packet<PacketWrenchMode> {

    private int slot;
    private boolean direction;

    public PacketWrenchMode(int slot, boolean direction) {

        this.slot = slot;
        this.direction = direction;
    }

    public PacketWrenchMode() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        ItemStack item = player.inventory.getStackInSlot(slot);
        if (item != null && item.getItem() instanceof ItemWrench) {
            int damage = (item.getItemDamage() + (direction ? 1 : -1)) % 4;
            if (damage < 0)
                damage += 4;
            item.setItemDamage(damage);
        }
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        slot = buffer.readInt();
        direction = buffer.readBoolean();
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        buffer.writeInt(slot);
        buffer.writeBoolean(direction);
    }

}
