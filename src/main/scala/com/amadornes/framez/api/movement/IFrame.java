package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

public interface IFrame extends IFrameRenderData, ISticky {

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public int getMultipartCount();

    public void cloneFrame(IFrame frame);

    public void harvest();

    public static interface IFrameBlock extends IFrameBlockRenderData, ISticky {

        public int getMultipartCount(World world, BlockPos position);

        public void cloneFrame(World world, BlockPos position, IFrame frame);

        public void cloneFrameBlock(World world, BlockPos position, World frameWorld, BlockPos framePosition, IFrameBlock frame);

        public void harvest(World world, BlockPos position);

    }

    public static interface IFrameProvider {

        public IFrame getFrameAt(World world, BlockPos position);

    }

}
