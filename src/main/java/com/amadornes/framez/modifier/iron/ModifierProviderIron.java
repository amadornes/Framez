package com.amadornes.framez.modifier.iron;

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

public class ModifierProviderIron implements IFrameModifierProvider {

    @SideOnly(Side.CLIENT)
    protected static IIcon border;
    @SideOnly(Side.CLIENT)
    protected static IIcon cross;
    @SideOnly(Side.CLIENT)
    protected static IIcon crossBlocked;

    @Override
    public String getIdentifier() {

        return Modifiers.IRON;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "framez.modifier.iron";
    }

    @Override
    public IFrameModifier instantiate(IFrame frame) {

        return new ModifierIron(frame, this);
    }

    @Override
    public boolean isCompatibleWith(String[] otherModifiers) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        border = reg.registerIcon(ModInfo.MODID + ":frameBorderIron");
        cross = reg.registerIcon(ModInfo.MODID + ":frameCrossIron");
        crossBlocked = reg.registerIcon(ModInfo.MODID + ":frameCrossIronClosed");
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

    private ModifierRecipeIron recipe = new ModifierRecipeIron();

    @Override
    public IFrameModifierRecipe getRecipeProvider() {

        return recipe;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 5;
    }

}
