package com.amadornes.framez.modifier.glass.clear;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierRecipe;
import com.amadornes.framez.modifier.glass.ModifierProviderGlass;
import com.amadornes.framez.ref.References.Modifiers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModifierProviderGlassClear extends ModifierProviderGlass {

    @Override
    public String getIdentifier() {

        return Modifiers.CLEAR_GLASS;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "framez.modifier.glass.clear";
    }

    @Override
    public IFrameModifier instantiate(IFrame frame) {

        return new ModifierGlassClear(frame, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

    }

    private ModifierRecipeGlassClear recipe = new ModifierRecipeGlassClear();

    @Override
    public IFrameModifierRecipe getRecipeProvider() {

        return recipe;
    }

}
