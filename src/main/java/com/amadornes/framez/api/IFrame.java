package com.amadornes.framez.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IFrame {

    public World world();

    public int x();

    public int y();

    public int z();

    public IFrameModifier[] getModifiers();

    public void addModifier(IFrameModifier modifier);

    public void addModifiers(IFrameModifier... modifiers);

    public void removeModifier(String modifier);

    public void writeModifiersToNBT(NBTTagCompound tag);

    public boolean hasModifier(String modifier);

    public void readModifiersFromNBT(NBTTagCompound tag);

    public Object[] getConnections();

    public boolean isSideBlocked(ForgeDirection side);

    public boolean toggleBlock(ForgeDirection side);

    public int getMaxCarriedBlocks();

}
