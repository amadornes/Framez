package com.amadornes.framez.movement.handler;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;
import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.ISticky;

public class StickyHandlerFMP implements ISticky {

    private static final StickyHandlerFMP instance = new StickyHandlerFMP();

    public static StickyHandlerFMP instance() {

        return instance;
    }

    @Override
    public boolean isSideSticky(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return false;

        boolean is = false;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof TSlottedPart) {
                int slot = ((TSlottedPart) p).getSlotMask();
                if (PartMap.face(side.ordinal()).mask == slot) {
                    if (p instanceof ISticky) {
                        return ((ISticky) p).isSideSticky(world, x, y, z, side, movement);
                    } else if (p instanceof FaceMicroblock || p instanceof HollowMicroblock) {
                        if (((CommonMicroblock) p).getSize() == 1)
                            return false;
                    }
                } else if (slot == PartMap.CENTER.mask && p instanceof ISticky)
                    return ((ISticky) p).isSideSticky(world, x, y, z, side, movement);
            } else if (p instanceof ISticky)
                is |= ((ISticky) p).isSideSticky(world, x, y, z, side, movement);
        }

        return is;
    }
}
