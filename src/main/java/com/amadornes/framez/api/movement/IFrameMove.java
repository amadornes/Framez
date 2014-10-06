package com.amadornes.framez.api.movement;

import net.minecraftforge.common.util.ForgeDirection;

public interface IFrameMove {

    /**
     * Returns whether or not this TileEntity can be moved
     */
    public boolean canBeMoved(ForgeDirection face, ForgeDirection direction);

}
