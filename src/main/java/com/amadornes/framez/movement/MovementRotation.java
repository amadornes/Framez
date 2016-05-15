package com.amadornes.framez.movement;

import net.minecraft.util.math.BlockPos;

public class MovementRotation implements IMovement {

    public MovementRotation() {
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        return pos;
    }

}
