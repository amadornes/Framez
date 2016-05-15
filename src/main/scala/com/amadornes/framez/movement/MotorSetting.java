package com.amadornes.framez.movement;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.tile.TileMotor;

/**
 * Class that represents a motor setting.
 */
public abstract class MotorSetting<T> {

    @SuppressWarnings("serial")
    public static final List<MotorSetting<?>> ALL_SETTINGS = new ArrayList<MotorSetting<?>>() {

        @Override
        public MotorSetting<?> remove(int id) {

            return null;
        }

        @Override
        public boolean remove(Object o) {

            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {

            return false;
        }

        @Override
        public void clear() {

        }
    };

    public static final MotorSetting<Boolean> REDSTONE_PULSE = new MotorSettingBoolean("redstone_pulse", false);
    public static final MotorSetting<RedstoneMode> REDSTONE_MODE = new MotorSettingRedstoneMode("redstone_mode", RedstoneMode.NORMAL);
    public static final MotorSetting<Double> MOVEMENT_DURATION = new MotorSettingDouble("movement_duration", 20D);

    static {
        ALL_SETTINGS.add(REDSTONE_PULSE);
        ALL_SETTINGS.add(REDSTONE_MODE);
        ALL_SETTINGS.add(MOVEMENT_DURATION);
    }

    @SuppressWarnings("unchecked")
    public static <T> MotorSetting<T> findSetting(String name, Class<T> type) {

        for (MotorSetting<?> s : ALL_SETTINGS)
            if (s.getName().equals(name))
                return (MotorSetting<T>) s;
        return null;
    }

    public static MotorSetting<?> findSetting(String name) {

        for (MotorSetting<?> s : ALL_SETTINGS)
            if (s.getName().equals(name))
                return s;
        return null;
    }

    private final String name;
    private final T def;

    public MotorSetting(String name, T def) {

        this.name = name;
        this.def = def;
    }

    public String getName() {

        return name;
    }

    public T getDefault(TileMotor motor) {

        return def;
    }

    @SuppressWarnings("unchecked")
    public T cast(Object obj) {

        return (T) obj;
    }

    public abstract void writeToNBT(T value, NBTTagCompound tag);

    public abstract T readFromNBT(NBTTagCompound tag);

    public abstract void writePacket(T value, ByteBuf buf);

    public abstract T readPacket(ByteBuf buf);

    public static final class MotorSettingBoolean extends MotorSetting<Boolean> {

        public MotorSettingBoolean(String name, Boolean def) {

            super(name, def);
        }

        @Override
        public void writeToNBT(Boolean value, NBTTagCompound tag) {

            tag.setBoolean("value", value);
        }

        @Override
        public Boolean readFromNBT(NBTTagCompound tag) {

            return tag.getBoolean("value");
        }

        @Override
        public void writePacket(Boolean value, ByteBuf buf) {

            buf.writeBoolean(value);
        }

        @Override
        public Boolean readPacket(ByteBuf buf) {

            return buf.readBoolean();
        }

    }

    public static final class MotorSettingInt extends MotorSetting<Integer> {

        public MotorSettingInt(String name, Integer def) {

            super(name, def);
        }

        @Override
        public void writeToNBT(Integer value, NBTTagCompound tag) {

            tag.setInteger("value", value);
        }

        @Override
        public Integer readFromNBT(NBTTagCompound tag) {

            return tag.getInteger("value");
        }

        @Override
        public void writePacket(Integer value, ByteBuf buf) {

            buf.writeInt(value);
        }

        @Override
        public Integer readPacket(ByteBuf buf) {

            return buf.readInt();
        }

    }

    public static final class MotorSettingDouble extends MotorSetting<Double> {

        public MotorSettingDouble(String name, Double def) {

            super(name, def);
        }

        @Override
        public void writeToNBT(Double value, NBTTagCompound tag) {

            tag.setDouble("value", value);
        }

        @Override
        public Double readFromNBT(NBTTagCompound tag) {

            return tag.getDouble("value");
        }

        @Override
        public void writePacket(Double value, ByteBuf buf) {

            buf.writeDouble(value);
        }

        @Override
        public Double readPacket(ByteBuf buf) {

            return buf.readDouble();
        }

    }

    public static final class MotorSettingRedstoneMode extends MotorSetting<RedstoneMode> {

        public MotorSettingRedstoneMode(String name, RedstoneMode def) {

            super(name, def);
        }

        @Override
        public void writeToNBT(RedstoneMode value, NBTTagCompound tag) {

            tag.setInteger("value", value.ordinal());
        }

        @Override
        public RedstoneMode readFromNBT(NBTTagCompound tag) {

            return RedstoneMode.values()[tag.getInteger("value")];
        }

        @Override
        public void writePacket(RedstoneMode value, ByteBuf buf) {

            buf.writeInt(value.ordinal());
        }

        @Override
        public RedstoneMode readPacket(ByteBuf buf) {

            return RedstoneMode.values()[buf.readInt()];
        }

    }

}
