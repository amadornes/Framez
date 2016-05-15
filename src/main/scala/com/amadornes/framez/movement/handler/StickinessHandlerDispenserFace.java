package com.amadornes.framez.movement.handler;

import net.minecraft.block.BlockDispenser;
import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky.IAdvancedSticky;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.trajectory.api.vec.BlockPos;

public class StickinessHandlerDispenserFace implements IStickinessHandler, IAdvancedSticky {

    @Override
    public boolean canHandle(World world, BlockPos position) {

        return world.getBlock(position.x, position.y, position.z) instanceof BlockDispenser;
    }

    @Override
    public boolean providesFinalValues() {

        return false;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return false;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return true;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public int getSideStickiness(World world, BlockPos position, int side) {

        return 0;
    }

    @Override
    public int getRequiredStickiness(World world, BlockPos position, int side) {

        return (world.getBlockMetadata(position.x, position.y, position.z) & 7) == side ? 10 : 0;
    }
}
