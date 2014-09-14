package com.amadornes.framez.modifier.glass;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.References.Modifiers;

public class ModifierGlass implements IFrameModifier {

    @SuppressWarnings("unused")
    private IFrame frame;
    private IFrameModifierProvider provider;

    public ModifierGlass(IFrame frame, IFrameModifierProvider provider) {

        this.frame = frame;
        this.provider = provider;
    }

    @Override
    public String getIdentifier() {

        return Modifiers.GLASS;
    }

    @Override
    public String getUnlocalizedName() {

        return "framez.modifier.glass";
    }

    @Override
    public double getHardnessMultiplier() {

        return 1;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public IIcon getCrossTexture(ForgeDirection side) {

        return ModifierProviderGlass.cross;
    }

    @Override
    public IIcon getBorderTexture(ForgeDirection side) {

        return ModifierProviderGlass.border;
    }

    @Override
    public boolean shouldRenderCross(ForgeDirection side) {

        return true;
    }

    @Override
    public IFrameModifierProvider getProvider() {

        return provider;
    }

    @Override
    public boolean canBlockSide(ForgeDirection side) {

        return false;
    }

    @Override
    public Block getMaterialType() {

        return Blocks.glass;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 0;
    }

}
