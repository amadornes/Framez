package com.amadornes.framez.frame;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.References.Modifiers;

public class ModifierProviderConnected implements IFrameModifierProvider {

    @Override
    public String getIdentifier() {

        return Modifiers.CONNECTED;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "Connected";
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

}
