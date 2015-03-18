package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.movement.FrameMovementRegistry;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketSingleBlockSync extends LocatedPacket<PacketSingleBlockSync> {

    private MovingBlock block;

    private Vec3i v;
    private NBTTagCompound t;

    public PacketSingleBlockSync(TileMotor motor, MovingBlock block) {

        super(motor.getX(), motor.getY(), motor.getZ());

        this.block = block;
    }

    public PacketSingleBlockSync() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;
        TileMotor te = (TileMotor) tile;

        MovingStructure s = te.getStructure();
        if (s == null)
            return;
        block = s.getBlock(v.getX(), v.getY(), v.getZ());
        if (block == null)
            return;
        FrameMovementRegistry.instance().readInfo(block, t);
        block.setRenderList(-1);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        buf.writeInt(block.getX());
        buf.writeInt(block.getY());
        buf.writeInt(block.getZ());
        NBTTagCompound t = FrameMovementRegistry.instance().writeInfo(block);
        ByteBufUtils.writeTag(buf, t);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        v = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
        t = ByteBufUtils.readTag(buf);
    }

}
