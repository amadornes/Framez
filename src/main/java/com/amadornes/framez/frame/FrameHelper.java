package com.amadornes.framez.frame;

import com.amadornes.framez.api.frame.EnumFramePart;
import com.amadornes.framez.api.frame.IFrame;
import com.amadornes.framez.api.frame.IFrameMaterial;

import net.minecraft.item.ItemStack;

public class FrameHelper {

    public static IFrameMaterial getMaterial(ItemStack stack) {

        for (IFrameMaterial mat : FrameRegistry.INSTANCE.getMaterials().values())
            if (mat.isMaterialItem(stack)) return mat;
        return null;
    }

    public static int getMaxCarriedBlocks(IFrame frame) {

        if (frame == null) return 0;
        return frame.getBorderMaterial().getMaxCarriedBlocks();
    }

    public static int getMaxCarriedParts(IFrame frame) {

        if (frame == null) return 0;
        return frame.getBorderMaterial().getMaxCarriedParts();
    }

    public static int getMinMovementTime(IFrame frame) {

        if (frame == null) return -1;
        return frame.getBorderMaterial().getMinMovementTime();
    }

    public static float getWeight(IFrame frame) {

        if (frame == null) return 0;
        return frame.getBorderMaterial().getWeight(EnumFramePart.BORDER)//
                + frame.getCrossMaterial().getWeight(EnumFramePart.CROSS)//
                + frame.getBindingMaterial().getWeight(EnumFramePart.BINDING);
    }

}
