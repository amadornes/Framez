package com.amadornes.framez.movement;

import net.minecraft.world.IBlockAccess;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FrameBlockRenderDataWrapper implements IFrameRenderData {

    protected final IBlockAccess world;
    protected final BlockPos position;
    protected final IFrameBlockRenderData renderData;

    public FrameBlockRenderDataWrapper(IBlockAccess world, BlockPos position, IFrameBlockRenderData renderData) {

        this.world = world;
        this.position = position;
        this.renderData = renderData;
    }

    @Override
    public boolean canHaveCovers() {

        return renderData.canHaveCovers();
    }

    @Override
    public boolean hasPanel(int side) {

        return renderData.hasPanel(world, position, side);
    }

    @Override
    public boolean shouldRenderCross(int side) {

        return renderData.shouldRenderCross(world, position, side);
    }

    @Override
    public boolean isSideBlocked(int side) {

        return renderData.isSideBlocked(world, position, side);
    }

    @Override
    public boolean isSideHidden(int side) {

        return renderData.isSideHidden(world, position, side);
    }

    @Override
    public IFrameMaterial getMaterial() {

        return renderData.getMaterial(world, position);
    }
}
