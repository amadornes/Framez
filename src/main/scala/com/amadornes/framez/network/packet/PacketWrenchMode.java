package com.amadornes.framez.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.item.ItemWrench;
import com.amadornes.framez.network.Packet;

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
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        slot = buf.readInt();
        direction = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(slot);
        buf.writeBoolean(direction);
    }

}
