package com.amadornes.framez.frame;

import java.util.List;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.EnumFramePart;
import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class FrameMaterialBasic implements IFrameMaterial {

    private final String type;
    private final float weight;
    private final int minMovTime, maxCarriedBlock, maxCarriedParts;
    private List<ItemStack> oredictEntries;

    public FrameMaterialBasic(String type, float weight, int minMovTime, int maxCarriedBlock, int maxCarriedParts, String oredictName) {

        this.type = type;
        this.weight = weight;
        this.minMovTime = minMovTime;
        this.maxCarriedBlock = maxCarriedBlock;
        this.maxCarriedParts = maxCarriedParts;
        this.oredictEntries = OreDictionary.getOres(oredictName);
    }

    @Override
    public String getType() {

        return type;
    }

    @Override
    public boolean canBeUsedAs(EnumFramePart part) {

        return true;
    }

    @Override
    public float getWeight(EnumFramePart part) {

        return weight * part.getDefaultWeightMultiplier();
    }

    @Override
    public int getMinMovementTime() {

        return minMovTime;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return maxCarriedBlock;
    }

    @Override
    public int getMaxCarriedParts() {

        return maxCarriedParts;
    }

    @Override
    public boolean isMaterialItem(ItemStack stack) {

        return OreDictionary.containsMatch(false, oredictEntries, stack);
    }

    @Override
    public ResourceLocation getTexture(EnumFrameTexture texture) {

        return new ResourceLocation(ModInfo.MODID, "blocks/frame/" + type + "/" + texture.name().toLowerCase());
    }

}
