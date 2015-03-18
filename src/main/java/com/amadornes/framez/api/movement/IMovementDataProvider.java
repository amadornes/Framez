package com.amadornes.framez.api.movement;

import net.minecraft.nbt.NBTTagCompound;

public interface IMovementDataProvider {

    public String getID();

    public boolean canHandle(IMovingBlock block);

    public void writeMovementInfo(IMovingBlock block, NBTTagCompound tag);

    public void readMovementInfo(IMovingBlock block, NBTTagCompound tag);

}