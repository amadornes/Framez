package com.amadornes.trajectory.network.packet;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.movement.MovementManager;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.network.Packet;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketSingleBlockSync extends Packet<PacketSingleBlockSync> {

    private MovingBlock block;

    public PacketSingleBlockSync(MovingBlock block) {

        this.block = block;
    }

    public PacketSingleBlockSync() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        // Write structure ID
        ByteBufUtils.writeUTF8String(buf, block.getStructure().getId().toString());

        // Write block coords
        buf.writeInt(block.getPosition().x);
        buf.writeInt(block.getPosition().y);
        buf.writeInt(block.getPosition().z);

        // Write block data
        ByteBufUtils.writeTag(buf, MovementManager.instance.describeBlock(block));
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        // Read structure ID
        String id = ByteBufUtils.readUTF8String(buf);

        // Read special tags
        int x = buf.readInt(), y = buf.readInt(), z = buf.readInt();

        // Find structure and block
        MovingStructure structure = MovementManager.instance.findStructure(true, UUID.fromString(id));
        if (structure == null)
            return;
        IMovingBlock block = structure.getBlocks().find(x, y, z);
        if (block == null)
            structure.addBlock((MovingBlock) (block = new MovingBlock(structure.getWorld(), new BlockPos(x, y, z))));

        // Read block data
        MovementManager.instance.readDescription(block, ByteBufUtils.readTag(buf));

        // Mark re-render
        structure.scheduleReRender();
    }

}
