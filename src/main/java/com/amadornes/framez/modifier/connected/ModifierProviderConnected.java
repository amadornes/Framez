package com.amadornes.framez.modifier.connected;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.api.IFrameModifierRecipe;
import com.amadornes.framez.ref.References.Modifiers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModifierProviderConnected implements IFrameModifierProvider {

    @Override
    public String getIdentifier() {

        return Modifiers.CONNECTED;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "framez.modifier.connected";
    }

    @Override
    public IFrameModifier instantiate(IFrame frame) {

        return new ModifierConnected(frame, this);
    }

    @Override
    public boolean isCompatibleWith(String[] otherModifiers) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

    }

    @Override
    public boolean overridesBorderTexture() {

        return false;
    }

    @Override
    public boolean overridesCrossTexture() {

        return false;
    }

    @Override
    public int overridePriorityBorder() {

        return 0;
    }

    @Override
    public int overridePriorityCross() {

        return 0;
    }

    private ModifierRecipeConnected recipe = new ModifierRecipeConnected();

    @Override
    public IFrameModifierRecipe getRecipeProvider() {

        return recipe;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return -1;
    }

}
