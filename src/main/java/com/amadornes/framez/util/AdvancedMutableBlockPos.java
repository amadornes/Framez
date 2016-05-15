package com.amadornes.framez.util;

import com.amadornes.framez.movement.MovingBlock;

import net.minecraft.util.math.BlockPos;

public final class AdvancedMutableBlockPos extends BlockPos {

    private int x;
    private int y;
    private int z;

    public AdvancedMutableBlockPos() {

        super(0, 0, 0);
    }

    @Override
    public int getX() {

        return this.x;
    }

    @Override
    public int getY() {

        return this.y;
    }

    @Override
    public int getZ() {

        return this.z;
    }

    public AdvancedMutableBlockPos set(BlockPos pos) {

        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public AdvancedMutableBlockPos set(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public BlockPos toImmutable() {

        return new BlockPos(this);
    }

    @Override
    public boolean equals(Object o) {

        return super.equals(((MovingBlock) o).pos);
    }

}