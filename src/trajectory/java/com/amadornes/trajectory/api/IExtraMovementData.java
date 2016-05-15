package com.amadornes.trajectory.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Class that stores extra, mod-provided data for movement. This gets synced to the client for things like custom rendering.
 */
public interface IExtraMovementData {

    /**
     * Writes the data to NBT.
     */
    public void writeToNBT(NBTTagCompound tag);

    /**
     * Reads the data from NBT.
     */
    public void readFromNBT(NBTTagCompound tag);

}
