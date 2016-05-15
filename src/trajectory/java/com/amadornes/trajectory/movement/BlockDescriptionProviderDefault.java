package com.amadornes.trajectory.movement;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.IMovingBlock;

public class BlockDescriptionProviderDefault extends BlockDescriptionProviderBase {

    @Override
    public int priority() {

        return Integer.MIN_VALUE;
    }

    @Override
    public String getType() {

        return "default";
    }

    @Override
    public boolean canHandle(IMovingBlock block) {

        return true;
    }

    @Override
    public void writeBlockData(IMovingBlock block, NBTTagCompound tag) {

        super.writeBlockData(block, tag);
        if (block.getTileEntity() != null) {
            Packet pkt = block.getTileEntity().getDescriptionPacket();
            if (pkt != null && pkt instanceof S35PacketUpdateTileEntity) {
                try {
                    ByteBuf buf = Unpooled.buffer();
                    pkt.writePacketData(new PacketBuffer(buf));
                    tag.setByteArray("tileData", buf.array());
                    tag.setString("tileDataType", pkt.getClass().getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void readBlockData(IMovingBlock block, NBTTagCompound tag) {

        super.readBlockData(block, tag);

        TileEntity te = block.getTileEntity();
        if (te != null && tag.hasKey("tileData")) {
            try {
                S35PacketUpdateTileEntity pkt = (S35PacketUpdateTileEntity) Class.forName(tag.getString("tileDataType")).newInstance();
                pkt.readPacketData(new PacketBuffer(Unpooled.copiedBuffer(tag.getByteArray("tileData"))));
                te.onDataPacket(null, pkt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
