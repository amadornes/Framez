package com.amadornes.framez.compat.da;

import mods.immibis.chunkloader.BlockChunkLoader;

import com.amadornes.framez.api.movement.IMovingBlock;
import com.amadornes.framez.movement.handler.NoInvalidateHandler;

public class MovementHandlerDA extends NoInvalidateHandler {

    @Override
    public boolean shouldHandle(IMovingBlock block) {

        return block.getBlock() instanceof BlockChunkLoader;
    }

}