package com.amadornes.framez.api.movement;

import net.minecraft.block.Block;

public interface IMovementApi {

    public void registerMovementHandler(IMovementHandler handler);

    public IMovementHandler[] getRegisteredHandlers();

    public void setBlockMovementType(Block block, BlockMovementType type);

    public void setBlockMovementType(Block block, int metadata, BlockMovementType type);

}
