package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IFrame.IFrameProvider;
import com.amadornes.framez.api.movement.ISticky.IStickinessHandler;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.vec.BlockPos;

public interface IMovementRegistry {

    public void registerStickinessHandler(IStickinessHandler handler);

    public void registerFrameProvider(IFrameProvider provider);

    public void registerStructureMovementHandler(IStructureMovementHandler handler);

    public void registerMultiblockMovementHandler(IMultiblockMovementHandler handler);

    public BlockSet findBlocks(World world, BlockPos position, IMovement movement);

    public BlockSet findBlocks(World world, BlockPos position, IMovement movement, IgnoreMode ignoreMode, BlockPos... ignored);

    public static enum IgnoreMode {

        PASS_THROUGH, AVOID;

    }

}
