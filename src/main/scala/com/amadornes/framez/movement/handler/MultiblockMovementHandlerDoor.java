package com.amadornes.framez.movement.handler;

import net.minecraft.block.BlockDoor;
import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IMultiblockMovementHandler;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;

public class MultiblockMovementHandlerDoor implements IMultiblockMovementHandler {

    @Override
    public void addBlocks(World world, BlockSet blocks) {

        for (IMovingBlock b : blocks) {
            if (b.getBlock() instanceof BlockDoor) {
                if (world.getBlock(b.getPosition().x, b.getPosition().y - 1, b.getPosition().z) instanceof BlockDoor)
                    blocks.add(new BlockPos(b.getPosition().x, b.getPosition().y - 1, b.getPosition().z));
                if (world.getBlock(b.getPosition().x, b.getPosition().y + 1, b.getPosition().z) instanceof BlockDoor)
                    blocks.add(new BlockPos(b.getPosition().x, b.getPosition().y + 1, b.getPosition().z));
            }
        }
    }

}
