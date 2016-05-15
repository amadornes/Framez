package com.amadornes.framez.api.modifier;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.api.movement.IMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IMotorUpgrade {

    public String getType();

    public boolean isUpgradeStack(ItemStack stack);

    public boolean canApply(IMotor motor, ItemStack stack, EntityPlayer player);

    public boolean canApplyDirectly(IMotor motor, ItemStack stack, EntityPlayer player);

    public static interface IConfigurableMotorUpgrade<T> extends IMotorUpgrade {

        @SideOnly(Side.CLIENT)
        public GuiScreen getConfigGUI(IMotor motor, int slot, GuiScreen parent);

        public T createUpgradeData(IMotor motor);

        public void writeToNBT(NBTTagCompound tag, T data);

        public void readFromNBT(NBTTagCompound tag, T data);
    }

    public static interface IMotorUpgradeData {

        public IMotorUpgrade getUpgrade();

        public ItemStack getStack();

        public Object getData();
    }

}