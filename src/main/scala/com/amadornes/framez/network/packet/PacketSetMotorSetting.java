package com.amadornes.framez.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.framez.movement.MotorSetting;
import com.amadornes.framez.network.LocatedPacket;
import com.amadornes.framez.tile.TileMotor;

public class PacketSetMotorSetting extends LocatedPacket<PacketSetMotorSetting> {

    private MotorSetting<?> setting;
    private Object value;

    public PacketSetMotorSetting(TileMotor motor, MotorSetting<?> setting, Object value) {

        super(motor.getX(), motor.getY(), motor.getZ());
        this.setting = setting;
        this.value = value;
    }

    public PacketSetMotorSetting() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;

        conf((TileMotor) tile, setting, value);
        ((TileMotor) tile).sendUpdate();
    }

    private <T> void conf(TileMotor motor, MotorSetting<T> setting, Object value) {

        motor.setSetting(setting, setting.cast(value));
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        setting = MotorSetting.ALL_SETTINGS.get(buf.readInt());
        value = setting.readPacket(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(MotorSetting.ALL_SETTINGS.indexOf(setting));
        write(setting, buf);
    }

    private <T> void write(MotorSetting<T> setting, ByteBuf buf) {

        setting.writePacket(setting.cast(value), buf);
    }

}
