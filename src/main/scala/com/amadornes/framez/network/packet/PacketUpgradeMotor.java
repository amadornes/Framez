package com.amadornes.framez.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.network.LocatedPacket;
import com.amadornes.framez.tile.TileMotor;

public class PacketUpgradeMotor extends LocatedPacket<PacketUpgradeMotor> {

    private int slot, invslot;

    public PacketUpgradeMotor(TileMotor motor, int slot, int invslot) {

        super(motor.getX(), motor.getY(), motor.getZ());
        this.slot = slot;
        this.invslot = invslot;
    }

    public PacketUpgradeMotor() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(slot);
        buf.writeInt(invslot);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        slot = buf.readInt();
        invslot = buf.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);

        if (tile == null || !(tile instanceof TileMotor))
            return;

        TileMotor te = (TileMotor) tile;
        IMotorUpgradeData data = te.getUpgrades()[slot];

        if (invslot == -1) {
            if (data != null && player.inventory.addItemStackToInventory(data.getStack()))
                te.setUpgrade(slot, null, null);
        } else {
            if (data != null)
                return;
            ItemStack is = player.inventory.getStackInSlot(invslot);
            IMotorUpgrade upgrade = null;
            if (is != null) {
                for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                    if (u.isUpgradeStack(is)) {
                        upgrade = u;
                        break;
                    }
                }
            }
            if (upgrade == null || is == null)
                return;

            ItemStack item = is.copy();
            item.stackSize = 1;
            te.setUpgrade(slot, upgrade, item);
            is.stackSize--;
            if (is.stackSize <= 0)
                player.inventory.setInventorySlotContents(invslot, null);
        }
    }

}
