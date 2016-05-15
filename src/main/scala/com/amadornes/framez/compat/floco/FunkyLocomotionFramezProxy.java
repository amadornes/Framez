package com.amadornes.framez.compat.floco;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.movement.MovementHelper;
import com.amadornes.trajectory.api.vec.BlockPos;

import framesapi.IStickyBlock;

public class FunkyLocomotionFramezProxy implements IStickyBlock {

    public static IStickyBlock oldFMP = null;

    @Override
    public boolean isStickySide(World world, int x, int y, int z, ForgeDirection dir) {

        if (StickinesHandlerFLoco.calling)
            return false;
        if (oldFMP != null && oldFMP.isStickySide(world, x, y, z, dir))
            return true;
        return MovementHelper.canStick(world, new BlockPos(x, y, z), dir.ordinal(), null);
    }
}