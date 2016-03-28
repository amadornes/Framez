package com.amadornes.framez.movement;

import com.amadornes.framez.api.movement.IBlockTransformation.IBlockRotation;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockRotation implements IBlockRotation {

    private final BlockPos center;
    private final Vec3 centerV;
    private final EnumFacing.Axis axis;
    private final EnumFacing.AxisDirection direction;

    public BlockRotation(BlockPos center, EnumFacing.Axis axis, EnumFacing.AxisDirection direction) {

        this.center = center;
        this.centerV = new Vec3(center).addVector(0.5, 0.5, 0.5);
        this.axis = axis;
        this.direction = direction;
    }

    @Override
    public String getType() {

        return "translation";
    }

    @Override
    public void defaultTransform(World world, BlockPos pos) {

        world.getBlockState(pos).getBlock().rotateBlock(world, pos, EnumFacing.func_181076_a(getDirection(), getAxis()));
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        return pos;
    }

    @Override
    public Vec3 transform(Vec3 vec, boolean normal) {

        if (axis == Axis.X)
            return normal ? new Vec3(vec.xCoord, vec.zCoord, -vec.yCoord) : transform(vec.subtract(centerV), true).add(centerV);
        if (axis == Axis.Y)
            return normal ? new Vec3(vec.zCoord, vec.yCoord, -vec.xCoord) : transform(vec.subtract(centerV), true).add(centerV);
        if (axis == Axis.Z)
            return normal ? new Vec3(-vec.yCoord, vec.xCoord, vec.zCoord) : transform(vec.subtract(centerV), true).add(centerV);
        return null;// TODO Transform vector
    }

    @Override
    public EnumFacing transform(EnumFacing facing) {

        for (int i = 0; i < (direction == EnumFacing.AxisDirection.POSITIVE ? 1 : 3); i++)
            facing = facing.rotateAround(axis);
        return facing;
    }

    @Override
    public BlockPos getCenter() {

        return center;
    }

    @Override
    public EnumFacing.Axis getAxis() {

        return axis;
    }

    @Override
    public EnumFacing.AxisDirection getDirection() {

        return direction;
    }

}
