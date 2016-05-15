package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.amadornes.framez.api.compat.IFramePlacementHandler;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.trajectory.api.vec.BlockPos;

public class PlacementHandlerDefault implements IFramePlacementHandler {

    @Override
    public int priority() {

        return 0;
    }

    @Override
    public boolean canPlaceFrame(World world, BlockPos position, IFrame reference) {

        Block block = world.getBlock(position.x, position.y, position.z);
        return block.isAir(world, position.x, position.y, position.z) || block.isReplaceable(world, position.x, position.y, position.z);
    }

    @Override
    public boolean placeFrame(World world, BlockPos position, IFrame reference) {

        if (!canPlaceFrame(world, position, reference))
            return false;

        if (reference instanceof IModifiableFrame) {
            for (int side = 0; side < 6; side++)
                if (((IModifiableFrame) reference).getSideModifiers(side).length > 0)
                    return placeFrameTE(world, position, reference);
        }

        return placeFrameNoTE(world, position, reference);
    }

    private boolean placeFrameTE(World world, BlockPos position, IFrame reference) {

        if (!placeFrameNoTE(world, position, reference))
            return false;

        // TODO: Add TE and side modifiers!
        return true;
    }

    private boolean placeFrameNoTE(World world, BlockPos position, IFrame reference) {

        int iBlocked = ((reference.isSideBlocked(0) ? 1 : 0) << 0) + ((reference.isSideBlocked(1) ? 1 : 0) << 1)
                + ((reference.isSideBlocked(2) ? 1 : 0) << 2) + ((reference.isSideBlocked(3) ? 1 : 0) << 3)
                + ((reference.isSideBlocked(4) ? 1 : 0) << 4) + ((reference.isSideBlocked(5) ? 1 : 0) << 5);

        Block block = FramezBlocks.frames.get(FrameFactory.getIdentifier("frame"
                + (iBlocked < 16 ? "0" : (iBlocked < 32 ? "1" : (iBlocked < 48 ? "2" : "3"))), reference.getMaterial()));
        int metadata = iBlocked % 16;

        if (!world.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1).offset(position.x, position.y, position.z)))
            return false;
        if (world.isRemote)
            return true;
        if (!world.setBlock(position.x, position.y, position.z, block, metadata, 0))
            return false;
        world.setBlockMetadataWithNotify(position.x, position.y, position.z, metadata, 3);
        return true;
    }

}
