package com.amadornes.framez.api.movement;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameRenderData {

    public boolean canHaveCovers();

    public boolean hasPanel(int side);

    public boolean shouldRenderCross(int side);

    public boolean isSideBlocked(int side);

    public boolean isSideHidden(int side);

    public IFrameMaterial getMaterial();

    public static interface IFrameBlockRenderData {

        public boolean canHaveCovers();

        public boolean hasPanel(IBlockAccess world, BlockPos position, int side);

        public boolean shouldRenderCross(IBlockAccess world, BlockPos position, int side);

        public boolean isSideBlocked(IBlockAccess world, BlockPos position, int side);

        public boolean isSideHidden(IBlockAccess world, BlockPos position, int side);

        public IFrameMaterial getMaterial(IBlockAccess world, BlockPos position);

    }

    @SideOnly(Side.CLIENT)
    public static interface IFrameTexture {

        public IIcon border(boolean panel);

        public IIcon cross(boolean blocked);

        public IIcon full();

    }

}