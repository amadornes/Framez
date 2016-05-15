package com.amadornes.framez.compat.bc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.IPipeTile;
import buildcraft.api.transport.pluggable.IFacadePluggable;
import buildcraft.api.transport.pluggable.PipePluggable;

import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.trajectory.api.vec.BlockPos;

public class BCStickinessHandler implements IStickinessHandler {

    @Override
    public boolean canHandle(World world, BlockPos position) {

        TileEntity tile = world.getTileEntity(position.x, position.y, position.z);
        return tile != null && tile instanceof IPipeTile;
    }

    @Override
    public boolean providesFinalValues() {

        return false;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        IPipeTile te = (IPipeTile) world.getTileEntity(position.x, position.y, position.z);
        if (!te.hasPipePluggable(ForgeDirection.getOrientation(side)))
            return false;
        PipePluggable pluggable = te.getPipePluggable(ForgeDirection.getOrientation(side));
        return pluggable instanceof IFacadePluggable && ((IFacadePluggable) pluggable).getCurrentBlock() instanceof IFrameBlock;
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return true;
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return false;
    }

}
