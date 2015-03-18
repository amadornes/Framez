package com.amadornes.framez.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.tile.TileMotor;

public class MovementSlide implements IMovementSlide {

    private ForgeDirection direction;

    public MovementSlide(ForgeDirection direction) {

        this.direction = direction;
    }

    @Override
    public MovementType getMovementType() {

        return MovementType.SLIDE;
    }

    @Override
    public ForgeDirection getDirection() {

        return direction;
    }

    @Override
    public void setDirection(ForgeDirection direction) {

        this.direction = direction;
    }

    @Override
    public Vec3i transform(Vec3i position) {

        return position.getRelative(getDirection());
    }

    @Override
    public Vec3d transform(Vec3d position, double progress) {

        return position.clone()
                .add(getDirection().offsetX * progress, getDirection().offsetY * progress, getDirection().offsetZ * progress);
    }

    @Override
    public boolean rotate(IMotor mover, ForgeDirection axis) {

        TileMotor te = (TileMotor) mover;

        ForgeDirection face = te.getFace().getRotation(axis);
        ForgeDirection direction = getDirection().getRotation(axis);

        System.out.println(face + " " + direction);

        if (face == direction || face == direction.getOpposite() || face.getOpposite() == direction)
            return false;

        this.direction = direction;
        te.setFace(face);
        return true;
    }

    @Override
    public boolean clashes(ForgeDirection direction) {

        return direction == getDirection() || direction == getDirection().getOpposite();
    }

    @Override
    public IMovement clone() {

        return new MovementSlide(getDirection());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("direction", direction.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        direction = ForgeDirection.getOrientation(tag.getInteger("direction"));
    }

    @Override
    public boolean debug(World world, int x, int y, int z, ForgeDirection face, EntityPlayer player) {

        player.addChatMessage(new ChatComponentText("Movement direction: " + direction.name().toLowerCase()));

        return true;
    }

}
