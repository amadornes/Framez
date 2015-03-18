package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public class MovementHandlerFluid implements IMovementHandler {

    @Override
    @Priority(PriorityEnum.OVERRIDE)
    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        Block b = world.getBlock(x, y, z);

        if (b instanceof BlockFluidBase) {
            if (((BlockFluidBase) b).getFilledPercentage(world, x, y, z) == 1F)
                return BlockMovementType.SEMI_MOVABLE;
            return BlockMovementType.UNMOVABLE;
        }
        if (b instanceof BlockLiquid) {
            if (BlockLiquid.getLiquidHeightPercent(world.getBlockMetadata(x, y, z)) == 1 / 9F)
                return BlockMovementType.SEMI_MOVABLE;
            return BlockMovementType.UNMOVABLE;
        }

        return null;
    }

    @Override
    public boolean startMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public boolean finishMoving(IMovingBlock block) {

        return false;
    }

    @Override
    public boolean canHandle(World world, int x, int y, int z) {

        return getMovementType(world, x, y, z, null, null) != null;
    }

}
