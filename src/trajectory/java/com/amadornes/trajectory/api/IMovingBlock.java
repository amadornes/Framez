package com.amadornes.trajectory.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

/**
 * Interface that represents a moving block.
 */
public interface IMovingBlock {

    /**
     * Gets the world this block belongs in.
     */
    public World getWorld();

    /**
     * Gets (and loads) the fake world this block is in while moving.
     */
    public World getFakeWorld();

    /**
     * Gets the original position of this block.
     */
    public BlockPos getPosition();

    /**
     * Gets the {@link Block} represented by this object.
     */
    public Block getBlock();

    /**
     * Gets the metadata of this block.
     */
    public int getMetadata();

    /**
     * Gets this block's {@link TileEntity}.
     */
    public TileEntity getTileEntity();

    /**
     * Gets the {@link IMovingStructure} this block is a part of.
     */
    public IMovingStructure getStructure();

    /**
     * Sets a new {@link IExtraMovementData} in this block.
     */
    public IMovingBlock setExtraMovementData(String type, IExtraMovementData data);

    /**
     * Gets an {@link IExtraMovementData} that was previously added to this block.
     */
    public IExtraMovementData getExtraMovementData(String type);

    /**
     * Sets the {@link Block} represented by this object.
     */
    public IMovingBlock setBlock(Block block);

    /**
     * Sets the metadata of this block.
     */
    public IMovingBlock setMetadata(int meta);

    /**
     * Sets the {@link TileEntity} of this block.
     */
    public IMovingBlock setTileEntity(TileEntity tile);

    /**
     * Takes a snapshot of the block at the original world and position. Called before movement starts to store the block's data.
     */
    public IMovingBlock snapshot();

}
