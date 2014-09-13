package com.amadornes.framez.modifier.connected;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.References.Modifiers;

public class ModifierConnected implements IFrameModifier {

    @SuppressWarnings("unused")
    private IFrame frame;
    private IFrameModifierProvider provider;

    public ModifierConnected(IFrame frame, IFrameModifierProvider provider) {

        this.frame = frame;
        this.provider = provider;
    }

    @Override
    public String getIdentifier() {

        return Modifiers.CONNECTED;
    }

    @Override
    public String getUnlocalizedName() {

        return "framez.modifier.connected";
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

        return null;
    }

    @Override
    public IIcon getBorderTexture(ForgeDirection side) {

        return null;
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

        return null;
    }

}
