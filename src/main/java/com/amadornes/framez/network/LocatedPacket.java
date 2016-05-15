package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class LocatedPacket<T extends LocatedPacket<T>> extends Packet<T> {

    protected BlockPos pos;

    public LocatedPacket(TileEntity te) {

        this(te.getPos());
    }

    public LocatedPacket(BlockPos pos) {

        this.pos = pos;
    }

    public LocatedPacket() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        pos = BlockPos.fromLong(buf.readLong());
    }

    public TargetPoint getTargetPoint(World world, double range) {

        return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
    }

}
