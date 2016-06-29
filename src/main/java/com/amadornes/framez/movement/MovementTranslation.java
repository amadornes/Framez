package com.amadornes.framez.movement;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MovementTranslation implements IMovement {

    private final EnumFacing face;
    private final int distance;

    public MovementTranslation(EnumFacing face) {

        this(face, 1);
    }

    public MovementTranslation(EnumFacing face, int distance) {

        this.face = face;
        this.distance = distance;
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        return pos.offset(face, distance);
    }

}
