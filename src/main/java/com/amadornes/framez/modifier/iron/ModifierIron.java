package com.amadornes.framez.modifier.iron;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.References.Modifiers;

public class ModifierIron implements IFrameModifier {

    private IFrame frame;
    private IFrameModifierProvider provider;

    public ModifierIron(IFrame frame, IFrameModifierProvider provider) {

        this.frame = frame;
        this.provider = provider;
    }

    @Override
    public String getIdentifier() {

        return Modifiers.IRON;
    }

    @Override
    public String getUnlocalizedName() {

        return "framez.modifier.iron";
    }

    @Override
    public double getHardnessMultiplier() {

        return 2.5;
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

        return frame.isSideBlocked(side) ? ModifierProviderIron.crossBlocked : ModifierProviderIron.cross;
    }

    @Override
    public IIcon getBorderTexture(ForgeDirection side) {

        return ModifierProviderIron.border;
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

        return true;
    }

    @Override
    public Block getMaterialType() {

        return Blocks.iron_block;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 5;
    }

}
