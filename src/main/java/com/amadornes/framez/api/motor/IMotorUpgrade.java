package com.amadornes.framez.api.motor;

import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMotorUpgrade extends INBTSerializable<NBTTagCompound> {

    public ResourceLocation getType();

    public Map<? extends IMotorVariable<?>, Object> getProvidedVariables();

    public Map<ResourceLocation, ? extends IMotorTrigger> getProvidedTriggers();

    public boolean hasConfigGUI();

    public Container getGuiContainer(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public GuiScreen getConfigGUI(EntityPlayer player, GuiScreen parent);

    public boolean hasCapability(Capability<?> capability, EnumFacing side);

    public <T> T getCapability(Capability<T> capability, EnumFacing side);

    public <T> T alterValue(T value, IMotorVariable<T> variable);

    public int getAlterationPriority(IMotorVariable<?> variable);

}
