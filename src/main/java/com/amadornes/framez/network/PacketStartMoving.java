package com.amadornes.framez.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.MovementScheduler;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class PacketStartMoving extends LocatedPacket<PacketStartMoving> {

    private MovingStructure structure;

    private double speed;
    private List<Vec3i> blocks = new ArrayList<Vec3i>();

    public PacketStartMoving(TileMotor motor, MovingStructure structure) {

        super(motor.getX(), motor.getY(), motor.getZ());

        this.structure = structure;

        speed = structure.getSpeed();
        for (IMovingBlock b : structure.getBlocks())
            blocks.add(new Vec3i(b));
    }

    public PacketStartMoving() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        TileEntity tile = player.worldObj.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return;
        TileMotor te = (TileMotor) tile;

        structure = new MovingStructure(te, te.getMovement(), speed);
        for (Vec3i b : blocks)
            structure.addBlock(new MovingBlock(b.setWorld(player.worldObj), structure, null).snapshot());

        MovingStructure s = te.getStructure();
        if (s != null) {
            while (s.getProgress() < 1) {
                s.tick(Phase.START);
                s.tick(Phase.END);
            }
        }

        te.setStructure(structure);
        MovementScheduler.instance().addStructure(structure);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);

        buffer.writeDouble(speed);

        buffer.writeInt(blocks.size());
        for (Vec3i v : blocks) {
            buffer.writeInt(v.getX());
            buffer.writeInt(v.getY());
            buffer.writeInt(v.getZ());
        }
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);

        speed = buffer.readDouble();

        int amt = buffer.readInt();
        for (int i = 0; i < amt; i++)
            blocks.add(new Vec3i(buffer.readInt(), buffer.readInt(), buffer.readInt()));
    }

}
