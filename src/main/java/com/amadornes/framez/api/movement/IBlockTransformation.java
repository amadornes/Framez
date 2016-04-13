package com.amadornes.framez.api.movement;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IBlockTransformation {

    public String getType();

    public void defaultTransform(World world, BlockPos pos);

    public BlockPos transform(BlockPos pos);

    public Vec3 transform(Vec3 vec, boolean normal);

    public EnumFacing transform(EnumFacing facing);

    public static interface IBlockTranslation extends IBlockTransformation {

        public BlockPos getTranslation();

    }

    public static interface IBlockRotation extends IBlockTransformation {

        public BlockPos getCenter();

        public EnumFacing.Axis getAxis();

        public EnumFacing.AxisDirection getDirection();

    }

}
