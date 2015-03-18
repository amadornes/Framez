package com.amadornes.framez.api.movement;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.vec.IWorldLocation;

public interface IMovingBlock extends IWorldLocation {

    public IMovingStructure getStructure();

    public Block getBlock();

    public int getMetadata();

    public TileEntity getTileEntity();

    public IMovingBlock setBlock(Block block);

    public IMovingBlock setMetadata(int metadata);

    public IMovingBlock setTileEntity(TileEntity tileentity);

    public IMovingBlock snapshot();

    public void startMoving(boolean invalidate, boolean validate);

    public void finishMoving(boolean invalidate, boolean validate);

}
