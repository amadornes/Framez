package com.amadornes.framez.movement;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MovementTranslation implements IMovement {

    private final EnumFacing face;

    public MovementTranslation(EnumFacing face) {

        this.face = face;
    }

    @Override
    public BlockPos transform(BlockPos pos) {

        return pos.offset(face);
    }

}
