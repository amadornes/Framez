package com.amadornes.framez.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

@SuppressWarnings("rawtypes")
public abstract class LocatedPacket<T extends LocatedPacket> extends Packet<T> {

    protected int x, y, z;

    public LocatedPacket(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocatedPacket() {

    }

    @Override
    public void read(NBTTagCompound tag) {

        x = tag.getInteger("x");
        y = tag.getInteger("y");
        z = tag.getInteger("z");
    }

    @Override
    public void write(NBTTagCompound tag) {

        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);
    }

    public TargetPoint getTargetPoint(World world, double range) {

        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range);
    }

}
