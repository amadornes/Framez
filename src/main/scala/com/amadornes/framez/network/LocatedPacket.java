package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class LocatedPacket<T extends LocatedPacket<T>> extends Packet<T> {

    protected int x, y, z;

    public LocatedPacket(TileEntity te) {

        this(te.xCoord, te.yCoord, te.zCoord);
    }

    public LocatedPacket(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocatedPacket() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public TargetPoint getTargetPoint(World world, double range) {

        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range);
    }

}
