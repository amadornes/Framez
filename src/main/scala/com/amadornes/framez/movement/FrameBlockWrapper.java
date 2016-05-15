package com.amadornes.framez.movement;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FrameBlockWrapper extends FrameBlockRenderDataWrapper implements IFrame {

    private final World w;
    private final IFrameBlock frame;

    public FrameBlockWrapper(World world, BlockPos position, IFrameBlock frame) {

        super(world, position, frame);
        this.w = world;
        this.frame = frame;
    }

    @Override
    public World getWorld() {

        return w;
    }

    @Override
    public int getX() {

        return position.x;
    }

    @Override
    public int getY() {

        return position.y;
    }

    @Override
    public int getZ() {

        return position.z;
    }

    @Override
    public boolean isSideSticky(World world, BlockPos position, int side, IMovement movement) {

        return frame.isSideSticky(world, position, side, movement);
    }

    @Override
    public boolean canStickToSide(World world, BlockPos position, int side, IMovement movement) {

        return frame.canStickToSide(world, position, side, movement);
    }

    @Override
    public boolean canBeOverriden(World world, BlockPos position) {

        return frame.canBeOverriden(world, position);
    }

    @Override
    public int getMultipartCount() {

        return frame.getMultipartCount(w, position);
    }

    @Override
    public void cloneFrame(IFrame frame) {

        this.frame.cloneFrame(w, position, frame);
    }

    @Override
    public void harvest() {

        this.frame.harvest(w, position);
    }

}
