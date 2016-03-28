package com.amadornes.framez.frame;

import com.amadornes.framez.api.frame.IFrameMaterial;

import net.minecraft.item.ItemStack;

public class FrameHelper {

    public static IFrameMaterial getMaterial(ItemStack stack) {

        for (IFrameMaterial mat : FrameRegistry.INSTANCE.materials.values())
            if (mat.isMaterialItem(stack)) return mat;
        return null;
    }

}
