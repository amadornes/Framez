package com.amadornes.trajectory.movement;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.IDynamicWorldReference;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.block.TileMoving;

public class DynamicWorldReferenceMoving implements IDynamicWorldReference {

    @Override
    public World getWorld(IMovingBlock block) {

        if (block.getTileEntity() instanceof TileMoving && ((TileMoving) block.getTileEntity()).structure != null)
            return ((TileMoving) block.getTileEntity()).structure.getFakeWorld();

        return null;
    }

}
