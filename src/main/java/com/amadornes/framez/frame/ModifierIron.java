package com.amadornes.framez.frame;

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
    public int getColorMultiplier() {

        return 0xFFFFFF;
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

}
