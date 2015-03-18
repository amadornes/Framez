package com.amadornes.framez.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.framez.api.movement.IMotor;

public class MovementRotation implements IMovementRotation {

    private ForgeDirection axis;
    private Vec3i center;

    public MovementRotation(ForgeDirection axis, Vec3i center) {

        this.axis = axis;
        this.center = center;
    }

    @Override
    public MovementType getMovementType() {

        return MovementType.ROTATION;
    }

    @Override
    public ForgeDirection getAxis() {

        return axis;
    }

    @Override
    public void setAxis(ForgeDirection axis) {

        this.axis = axis;
    }

    @Override
    public Vec3i getCenter() {

        return center;
    }

    @Override
    public Vec3i transform(Vec3i position) {

        return position.clone();// FIXME Actually rotate
    }

    @Override
    public Vec3d transform(Vec3d position, double progress) {

        return position.clone();// FIXME Actually rotate
    }

    @Override
    public boolean rotate(IMotor mover, ForgeDirection axis) {

        this.axis = this.axis.getOpposite();

        return true;
    }

    @Override
    public boolean clashes(ForgeDirection direction) {

        return false;
    }

    @Override
    public IMovement clone() {

        return new MovementRotation(getAxis(), getCenter());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        // TODO Not really needed now
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        // TODO Not really needed now
    }

    @Override
    public boolean debug(World world, int x, int y, int z, ForgeDirection face, EntityPlayer player) {

        player.addChatMessage(new ChatComponentText("Movement axis: " + axis.name().toLowerCase()));

        return true;
    }

}
