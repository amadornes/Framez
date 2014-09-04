package com.amadornes.framez.network.packet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.movement.StructureTickHandler;
import com.amadornes.framez.network.LocatedPacket;
import com.amadornes.framez.tile.TileMotor;

public class PacketStartMoving extends LocatedPacket<PacketStartMoving> {

    private List<BlockCoord> blocks = new ArrayList<BlockCoord>();
    private double speed = 0;
    private ForgeDirection direction = ForgeDirection.SOUTH;

    public PacketStartMoving(TileMotor motor) {

        super(motor.xCoord, motor.yCoord, motor.zCoord);

        for (MovingBlock b : motor.getStructure().getBlocks())
            blocks.add(b.getLocation());
        speed = motor.getStructure().getSpeed();
        direction = motor.getDirection();
    }

    public PacketStartMoving() {

    }

    @Override
    public void handleClientSide(PacketStartMoving message, EntityPlayer player) {

        TileMotor motor = (TileMotor) player.worldObj.getTileEntity(x, y, z);
        MovingStructure structure = new MovingStructure(player.worldObj, direction, speed);
        structure.addBlocks(blocks);
        motor.setStructure(structure);
        StructureTickHandler.INST.addStructure(structure);
    }

    @Override
    public void handleServerSide(PacketStartMoving message, EntityPlayer player) {

    }

    @Override
    public void write(NBTTagCompound tag) {

        super.write(tag);

        NBTTagList list = new NBTTagList();
        for (BlockCoord b : blocks) {
            NBTTagCompound block = new NBTTagCompound();
            block.setInteger("x", b.x);
            block.setInteger("y", b.y);
            block.setInteger("z", b.z);
            list.appendTag(block);
        }
        tag.setTag("blocks", list);
        tag.setDouble("speed", speed);
        tag.setInteger("direction", direction.ordinal());
    }

    @Override
    public void read(NBTTagCompound tag) {

        super.read(tag);

        NBTTagList list = tag.getTagList("blocks", new NBTTagCompound().getId());
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound t = list.getCompoundTagAt(i);
            blocks.add(new BlockCoord(t.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")));
        }
        speed = tag.getDouble("speed");
        direction = ForgeDirection.getOrientation(tag.getInteger("direction"));
    }

}
