package com.amadornes.framez.api.frame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IFrameMaterial {

    public String getType();

    public boolean canBeUsedAs(EnumFramePart part);

    public double getWeight(EnumFramePart part);

    public int getMinMovementTime();

    public int getMaxCarriedBlocks();

    public int getMaxCarriedParts();

    public boolean isMaterialItem(ItemStack stack);

    public ResourceLocation getTexture(EnumFrameTexture part);

}
