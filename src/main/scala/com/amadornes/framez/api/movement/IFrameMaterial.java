package com.amadornes.framez.api.movement;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.movement.IFrameRenderData.IFrameTexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameMaterial {

    public String getType();

    @SideOnly(Side.CLIENT)
    public void registerTextures();

    @SideOnly(Side.CLIENT)
    public IFrameTexture getTexture(IFrameRenderData renderData, int side, int type);

    public int getMaxMovedBlocks();

    public int getMaxMultiparts();

    public int getMinMovementTime();

    public void registerRecipes(ItemStack frame);

}
