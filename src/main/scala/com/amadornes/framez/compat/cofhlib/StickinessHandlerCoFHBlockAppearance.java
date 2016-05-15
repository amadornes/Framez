package com.amadornes.framez.compat.cofhlib;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.block.IBlockAppearance;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.api.movement.ISticky.IAdvancedSticky;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.trajectory.api.vec.BlockPos;

public class StickinessHandlerCoFHBlockAppearance implements IStickinessHandler, IAdvancedSticky {

    @Override
    public boolean canHandle(World world, BlockPos position) {

        return world.getBlock(position.x, position.y, position.z) instanceof IBlockAppearance;
    }

    @Override
    public boolean providesFinalValues() {

        return false;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        ISticky sticky = getSticky(world, position, side);
        if (sticky != null)
            return sticky.isSideSticky(null, null, side, movement);
        return false;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        ISticky sticky = getSticky(world, position, side);
        if (sticky != null)
            return sticky.canStickToSide(null, null, side, movement);
        return true;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

    @Override
    public int getSideStickiness(World world, BlockPos position, int side) {

        ISticky sticky = getSticky(world, position, side);
        if (sticky != null && sticky instanceof IAdvancedSticky)
            return ((IAdvancedSticky) sticky).getSideStickiness(null, null, side);
        return 0;
    }

    @Override
    public int getRequiredStickiness(World world, BlockPos position, int side) {

        ISticky sticky = getSticky(world, position, side);
        if (sticky != null && sticky instanceof IAdvancedSticky)
            return ((IAdvancedSticky) sticky).getRequiredStickiness(null, null, side);
        return 0;
    }

    private ISticky getSticky(World world, BlockPos position, int side) {

        Block block = ((IBlockAppearance) world.getBlock(position.x, position.y, position.z)).getVisualBlock(world, position.x, position.y,
                position.z, ForgeDirection.getOrientation(side));
        if (block != null && block instanceof ISticky)
            return (ISticky) block;
        return null;
    }

}
