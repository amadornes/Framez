package com.amadornes.trajectory.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface used to describe blocks so they can be reconstructed on the client.
 */
public interface IBlockDescriptionProvider {

    /**
     * Gets a unique ID for this description provider. It'll be sent to the client along with the data and be used to retrieve an instance of this
     * class on the client.
     */
    public String getType();

    /**
     * Checks whether or not this class can describe the specified block.
     */
    public boolean canHandle(IMovingBlock block);

    /**
     * Writes the block's data to an NBT tag that gets sent to the client.
     */
    public void writeBlockData(IMovingBlock block, NBTTagCompound tag);

    /**
     * Reads the block's data from an NBT tag that's been sent from the server.
     */
    public void readBlockData(IMovingBlock block, NBTTagCompound tag);

    /**
     * Interface that allows for a custom block description system (with a specific priority over others).
     */
    public interface IPrioritisedBlockDescriptionProvider extends IBlockDescriptionProvider {

        /**
         * Gets the priority of this description provider. The default description provider has a priority of Integer.MIN_VALUE.
         */
        public int priority();

    }

}
