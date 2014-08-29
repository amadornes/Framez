package com.amadornes.framez.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameModifier {

    public String getIdentifier();

    public String getUnlocalizedName();

    public int getColorMultiplier();

    public double getHardnessMultiplier();

    public void onAdded();

    public void onRemoved();

    public void onUpdate();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    @SideOnly(Side.CLIENT)
    public IIcon getCrossTexture(ForgeDirection side);

    @SideOnly(Side.CLIENT)
    public IIcon getBorderTexture(ForgeDirection side);

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderCross(ForgeDirection side);

    public IFrameModifierProvider getProvider();

}