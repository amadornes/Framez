package com.amadornes.framez.modifier.glass;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.api.IFrameModifierRecipe;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References.Modifiers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModifierProviderGlass implements IFrameModifierProvider {

    @SideOnly(Side.CLIENT)
    public static IIcon border;
    @SideOnly(Side.CLIENT)
    protected static IIcon cross;

    @Override
    public String getIdentifier() {

        return Modifiers.GLASS;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "framez.modifier.glass";
    }

    @Override
    public IFrameModifier instantiate(IFrame frame) {

        return new ModifierGlass(frame, this);
    }

    @Override
    public boolean isCompatibleWith(String[] otherModifiers) {

        return true;
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        border = reg.registerIcon(ModInfo.MODID + ":frameBorderGlass");
        cross = reg.registerIcon(ModInfo.MODID + ":frameCrossGlass");
    }

    @Override
    public boolean overridesBorderTexture() {

        return true;
    }

    @Override
    public boolean overridesCrossTexture() {

        return true;
    }

    @Override
    public int overridePriorityBorder() {

        return 50;
    }

    @Override
    public int overridePriorityCross() {

        return 50;
    }

    private ModifierRecipeGlass recipe = new ModifierRecipeGlass();

    @Override
    public IFrameModifierRecipe getRecipeProvider() {

        return recipe;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 0;
    }

}
