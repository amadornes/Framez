package com.amadornes.framez.api.motor;

import net.minecraft.item.ItemStack;

public interface IMotorTrigger {

    public String getUnlocalizedName(boolean inverted);

    public boolean isActive();

    public boolean canBeInverted();

    public ItemStack getIconStack();

}
