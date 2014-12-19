package com.amadornes.framez.compat.rc;

import mods.railcraft.common.util.misc.IAnchor;

import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.handler.NoInvalidateHandler;

public class MovementHandlerRC extends NoInvalidateHandler {

    @Override
    public boolean shouldHandle(IMovingBlock block) {

        return block.getTileEntity() instanceof IAnchor;
    }

}