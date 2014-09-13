package com.amadornes.framez.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameModifierProvider {

    public String getIdentifier();

    public String getUnlocalizedName(ItemStack item);

    public IFrameModifier instantiate(IFrame frame);

    public boolean isCompatibleWith(String[] otherModifiers);

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg);

    public boolean overridesBorderTexture();

    public boolean overridesCrossTexture();

    public int overridePriorityBorder();

    public int overridePriorityCross();

    public IFrameModifierRecipe getRecipeProvider();

}
