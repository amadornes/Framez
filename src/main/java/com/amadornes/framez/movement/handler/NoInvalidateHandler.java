package com.amadornes.framez.movement.handler;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.HandlingPriority;
import com.amadornes.framez.api.movement.HandlingPriority.Priority;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public abstract class NoInvalidateHandler implements IMovementHandler {

    public abstract boolean shouldHandle(IMovingBlock block);

    @Override
    @HandlingPriority(Priority.HIGH)
    public boolean handleStartMoving(IMovingBlock block) {

        if (shouldHandle(block)) {
            block.remove_do(false, false);
            return true;
        }
        return false;
    }

    @Override
    @HandlingPriority(Priority.HIGH)
    public boolean handleFinishMoving(IMovingBlock block) {

        if (shouldHandle(block)) {
            block.place_do(false, false);
            return true;
        }
        return false;
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        return null;
    }

}