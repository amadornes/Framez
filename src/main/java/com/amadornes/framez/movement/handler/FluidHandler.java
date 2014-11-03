package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public class FluidHandler implements IMovementHandler {

    @Override
    public boolean handleStartMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        Block b = w.getBlock(x, y, z);

        if (b instanceof BlockFluidBase || b instanceof BlockDynamicLiquid)
            return BlockMovementType.SEMI_MOVABLE;

        return null;
    }

}
