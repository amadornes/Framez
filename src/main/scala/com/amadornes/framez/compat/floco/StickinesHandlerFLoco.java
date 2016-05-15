package com.amadornes.framez.compat.floco;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.rwtema.funkylocomotion.proxydelegates.ProxyRegistry;

import framesapi.IStickyBlock;

public class StickinesHandlerFLoco implements IStickinessHandler {

    public static boolean calling = false;

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        if (calling)
            return false;

        calling = true;
        boolean result = ProxyRegistry.getInterface(world.getBlock(position.x, position.y, position.z), IStickyBlock.class).isStickySide(
                world, position.x, position.y, position.z, ForgeDirection.getOrientation(side));
        calling = false;

        return result;
    }

    @Override
    public boolean providesFinalValues() {

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
    public boolean canHandle(World world, BlockPos position) {

        return ProxyRegistry.getInterface(world.getBlock(position.x, position.y, position.z), IStickyBlock.class) != null;
    }
}