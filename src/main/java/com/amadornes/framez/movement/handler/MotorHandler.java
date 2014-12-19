package com.amadornes.framez.movement.handler;

import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.block.BlockMotor;

public class MotorHandler extends NoInvalidateHandler {

    @Override
    public boolean shouldHandle(IMovingBlock block) {

        return block.getBlock() instanceof BlockMotor;
    }

}
