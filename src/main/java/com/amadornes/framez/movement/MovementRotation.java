package com.amadornes.framez.movement;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

public class MovementRotation implements IMovement {

    private final DynamicReference<TileMotor> motor;
    private final EnumFacing.Axis axis;
    private final EnumMotorAction action;

    public MovementRotation(DynamicReference<TileMotor> motor, EnumFacing.Axis axis, EnumMotorAction action) {

        this.motor = motor;
        this.axis = axis;
        this.action = action;
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        BlockPos origin = motor.get().getPos();
        BlockPos finalPos = pos.subtract(origin);
        if (axis == Axis.Y && action == EnumMotorAction.ROTATE_CLOCKWISE) {
            finalPos = new BlockPos(-finalPos.getZ(), finalPos.getY(), finalPos.getX());
        } else if (axis == Axis.Y && action == EnumMotorAction.ROTATE_CCLOCKWISE) {
            finalPos = new BlockPos(finalPos.getZ(), finalPos.getY(), -finalPos.getX());
        } else if (axis == Axis.Z && action == EnumMotorAction.ROTATE_CLOCKWISE) {
            finalPos = new BlockPos(finalPos.getY(), -finalPos.getX(), finalPos.getZ());
        } else if (axis == Axis.Z && action == EnumMotorAction.ROTATE_CCLOCKWISE) {
            finalPos = new BlockPos(-finalPos.getY(), finalPos.getX(), finalPos.getZ());
        } else if (axis == Axis.X && action == EnumMotorAction.ROTATE_CLOCKWISE) {
            finalPos = new BlockPos(finalPos.getX(), finalPos.getZ(), -finalPos.getY());
        } else if (axis == Axis.X && action == EnumMotorAction.ROTATE_CCLOCKWISE) {
            finalPos = new BlockPos(finalPos.getX(), -finalPos.getZ(), finalPos.getY());
        }
        return finalPos.add(origin);
    }

}
