package com.amadornes.framez.movement.handler;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;

public class BlockHandler implements IMovementHandler {

    private Block block;
    private int meta = -1;
    private BlockMovementType type;

    public BlockHandler(Block block, int meta, BlockMovementType type) {

        this.block = block;
        this.meta = meta;

        this.type = type;
    }

    public BlockHandler(Block block, BlockMovementType type) {

        this.block = block;

        this.type = type;
    }

    @Override
    public boolean handleStartMoving(IMovingBlock data) {

        return false;
    }

    @Override
    public boolean handleFinishMoving(IMovingBlock data) {

        return false;
    }

    @Override
    public BlockMovementType getMovementType(World w, Integer x, Integer y, Integer z) {

        Block b = w.getBlock(x, y, z);
        if (block != b)
            return null;
        if (meta != -1 && w.getBlockMetadata(x, y, z) != meta)
            return null;

        return type;
    }

}
