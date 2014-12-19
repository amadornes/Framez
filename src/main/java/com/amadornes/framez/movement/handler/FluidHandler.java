package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
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

        if (b instanceof BlockFluidBase) {
            if (((BlockFluidBase) b).getFilledPercentage(w, x, y, z) == 1F)
                return BlockMovementType.SEMI_MOVABLE;
            return BlockMovementType.REPLACEABLE;
        }
        if (b instanceof BlockLiquid) {
            if (BlockLiquid.getLiquidHeightPercent(w.getBlockMetadata(x, y, z)) == 1 / 9F)
                return BlockMovementType.SEMI_MOVABLE;
            return BlockMovementType.REPLACEABLE;
        }

        return null;
    }

}
