package com.amadornes.framez.compat.fl;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.util.Utils;
import com.rwtema.funkylocomotion.fmp.FMPStickness;

import framesapi.IStickyBlock;

public class FunkyLocomotionFramezProxy implements IStickyBlock {

    @Override
    public boolean isStickySide(World world, int x, int y, int z, ForgeDirection dir) {

        if (new FMPStickness().isStickySide(world, x, y, z, dir))
            return true;

        IFrame frame = Utils.getFrame(world, x, y, z);
        if (frame == null)
            return false;
        if (frame instanceof FunkyLocomotionFrame || frame instanceof FunkyLocomotionFrameMicroblock)
            return false;

        return !frame.isSideBlocked(dir);
    }

}
