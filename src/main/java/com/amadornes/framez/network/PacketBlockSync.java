package com.amadornes.framez.network;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.FrameMovementRegistry;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketBlockSync extends LocatedPacket<PacketBlockSync> {

    private MovingStructure structure;

    private Map<Vec3i, NBTTagCompound> data = new HashMap<Vec3i, NBTTagCompound>();

    public PacketBlockSync(TileMotor motor, MovingStructure structure) {

        super(motor.getX(), motor.getY(), motor.getZ());

        this.structure = structure;
    }

    public PacketBlockSync() {

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

        for (Vec3i v : data.keySet()) {
            MovingBlock b = s.getBlock(v.getX(), v.getY(), v.getZ());
            if (b == null)
                continue;
            FrameMovementRegistry.instance().readInfo(b, data.get(v));
            b.setRenderList(-1);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        List<IMovingBlock> l = structure.getBlocks();
        buf.writeInt(l.size());
        for (IMovingBlock b : l) {
            buf.writeInt(b.getX());
            buf.writeInt(b.getY());
            buf.writeInt(b.getZ());
            NBTTagCompound t = FrameMovementRegistry.instance().writeInfo(b);
            ByteBufUtils.writeTag(buf, t);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            Vec3i v = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
            NBTTagCompound t = ByteBufUtils.readTag(buf);
            data.put(v, t);
        }
    }

}
