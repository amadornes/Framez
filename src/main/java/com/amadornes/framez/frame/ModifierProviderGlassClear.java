package com.amadornes.framez.frame;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.ref.References.Modifiers;

public class ModifierProviderGlassClear extends ModifierProviderGlass {

    @Override
    public String getIdentifier() {

        return Modifiers.CLEAR_GLASS;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "Clear Glass";
    }

    @Override
    public IFrameModifier instantiate(IFrame frame) {

        return new ModifierGlassClear(frame, this);
    }

    @Override
    public void registerIcons(IIconRegister reg) {

    }

}
