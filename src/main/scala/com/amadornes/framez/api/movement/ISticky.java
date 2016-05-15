package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

public interface ISticky {

    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement);

    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement);

    public boolean canBeOverriden(World world, BlockPos position);

    public static interface IAdvancedSticky extends ISticky {

        public int getSideStickiness(World world, BlockPos position, int side);

        public int getRequiredStickiness(World world, BlockPos position, int side);

    }

    public static interface IStickinessHandler extends ISticky {

        public boolean canHandle(World world, BlockPos position);

        public boolean providesFinalValues();
    }

}
