package com.amadornes.trajectory.network.packet;

import io.netty.buffer.ByteBuf;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.trajectory.Trajectory;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryFeature;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.movement.MovementManager;
import com.amadornes.trajectory.movement.MovingBlock;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.network.Packet;

import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketStartMoving extends Packet<PacketStartMoving> {

    private MovingStructure structure;

    public PacketStartMoving(MovingStructure structure) {

        this.structure = structure;
    }

    public PacketStartMoving() {

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
        ByteBufUtils.writeUTF8String(buf, structure.getId().toString());

        // Write features
        int features = 0;
        for (TrajectoryFeature f : structure.getFeatures())
            features |= 1 >> f.ordinal();
        buf.writeInt(features);

        // Write trajectory type
        ByteBufUtils.writeUTF8String(buf, MovementManager.instance.getTrajectoryType(structure.getTrajectory()));
        // Write trajectory data
        NBTTagCompound trajectoryData = new NBTTagCompound();
        structure.getTrajectory().writeToNBT(trajectoryData);
        ByteBufUtils.writeTag(buf, trajectoryData);

        // Write blocks
        buf.writeInt(structure.getBlocks().size());
        for (IMovingBlock block : structure.getBlocks()) {
            buf.writeInt(block.getPosition().x);
            buf.writeInt(block.getPosition().y);
            buf.writeInt(block.getPosition().z);
            ByteBufUtils.writeTag(buf, MovementManager.instance.describeBlock((MovingBlock) block));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        // Read structure ID
        String id = ByteBufUtils.readUTF8String(buf);

        // Read features
        Set<TrajectoryFeature> features = new HashSet<TrajectoryFeature>();
        int featuresI = buf.readInt();
        for (TrajectoryFeature f : TrajectoryFeature.values())
            if ((featuresI & (1 << f.ordinal())) != 0)
                features.add(f);

        // Read trajectory type
        String type = ByteBufUtils.readUTF8String(buf);
        // Read trajectory data
        NBTTagCompound trajectoryData = ByteBufUtils.readTag(buf);

        // Read blocks
        BlockSet blocks = new BlockSet();
        int amt = buf.readInt();
        for (int i = 0; i < amt; i++) {
            int x = buf.readInt(), y = buf.readInt(), z = buf.readInt();
            blocks.add(MovementManager.instance.readDescription(Trajectory.proxy.getWorld(), new BlockPos(x, y, z),
                    ByteBufUtils.readTag(buf)));
        }

        // Recreate trajectory
        ITrajectory trajectory = MovementManager.instance.recreateTrajectory(type, trajectoryData);

        // Recreate structure
        MovingStructure structure = new MovingStructure(UUID.fromString(id), blocks, trajectory, features, null);
        MovementManager.instance.startMoving(structure);
    }

}
