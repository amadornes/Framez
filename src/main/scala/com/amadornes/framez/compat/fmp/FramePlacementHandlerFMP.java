package com.amadornes.framez.compat.fmp;

import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.compat.IFramePlacementHandler;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FramePlacementHandlerFMP implements IFramePlacementHandler {

    @Override
    public int priority() {

        return -50;
    }

    @Override
    public boolean placeFrame(World world, BlockPos position, IFrame reference) {

        TMultiPart part = PartFactory.inst.createPart(FrameFactory.getIdentifier(ModInfo.MODID + ":frame", reference.getMaterial()),
                world.isRemote);

        if (!canPlaceFrame(world, position, reference, part))
            return false;

        if (world.isRemote)
            return true;

        BlockCoord coord = new BlockCoord(position.x, position.y, position.z);
        ((IFrame) part).cloneFrame(reference);
        TileMultipart.addPart(world, coord, part);
        return true;
    }

    @Override
    public boolean canPlaceFrame(World world, BlockPos position, IFrame reference) {

        TMultiPart part = PartFactory.inst.createPart(FrameFactory.getIdentifier(ModInfo.MODID + ":frame", reference.getMaterial()),
                world.isRemote);

        return canPlaceFrame(world, position, reference, part);
    }

    private boolean canPlaceFrame(World world, BlockPos position, IFrame reference, TMultiPart part) {

        if (part == null)
            return false;

        BlockCoord coord = new BlockCoord(position.x, position.y, position.z);
        if (!TileMultipart.canPlacePart(world, coord, part))
            return false;

        return true;
    }
}
