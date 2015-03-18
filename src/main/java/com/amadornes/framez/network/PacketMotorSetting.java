package com.amadornes.framez.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.network.LocatedPacket;

import com.amadornes.framez.api.movement.MotorSetting;
import com.amadornes.framez.tile.TileMotor;

public class PacketMotorSetting extends LocatedPacket<PacketMotorSetting> {

    private MotorSetting setting;

    public PacketMotorSetting(TileMotor motor, MotorSetting setting) {

        super(motor);
        this.setting = setting;
    }

    public PacketMotorSetting() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;

        ((TileMotor) tile).configure(setting);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);

        setting = MotorSetting.values()[buffer.readInt()];
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);

        buffer.writeInt(setting.ordinal());
    }

}
