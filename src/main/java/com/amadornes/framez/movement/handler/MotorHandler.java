package com.amadornes.framez.movement.handler;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.block.BlockMotor;

public class MotorHandler implements IMovementHandler {

    @Override
    public boolean handleStartMoving(IMovingBlock block) {

        if (block.getBlock() instanceof BlockMotor) {
            block.remove_do(false, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock block) {

        if (block.getBlock() instanceof BlockMotor) {
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
