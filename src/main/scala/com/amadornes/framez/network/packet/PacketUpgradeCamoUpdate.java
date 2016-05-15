package com.amadornes.framez.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.network.LocatedPacket;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage.MotorUpgradeCamouflageData;

public class PacketUpgradeCamoUpdate extends LocatedPacket<PacketUpgradeCamoUpdate> {

    private int side, invslot, face;

    public PacketUpgradeCamoUpdate(IMotor motor, int side, int invslot, int face) {

        super(motor.getX(), motor.getY(), motor.getZ());
        this.side = side;
        this.invslot = invslot;
        this.face = face;
    }

    public PacketUpgradeCamoUpdate() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(side);
        buf.writeInt(invslot);
        buf.writeInt(face);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        side = buf.readInt();
        invslot = buf.readInt();
        face = buf.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);

        if (tile == null || !(tile instanceof IMotor))
            return;

        IMotor te = (IMotor) tile;

        IMotorUpgradeData upgradeData = null;
        for (int i = 0; i < 7; i++) {
            upgradeData = te.getUpgrades()[i];
            if (upgradeData != null && upgradeData.getUpgrade() instanceof MotorUpgradeCamouflage)
                break;
        }

        if (upgradeData == null)
            return;

        MotorUpgradeCamouflageData data = (MotorUpgradeCamouflageData) upgradeData.getData();

        if (invslot == -2) {
            data.setCamo(side, null, side);
            te.sendUpdate();
        } else if (invslot == -1) {
            data.setCamo(side, data.getCamo(side), face);
        } else {
            ItemStack stack = player.inventory.getStackInSlot(invslot);
            data.setCamo(side, stack == null ? null : stack.copy().splitStack(1), data.getCamoFace(side));
        }
    }

}
