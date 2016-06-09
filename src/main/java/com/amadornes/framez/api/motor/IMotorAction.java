package com.amadornes.framez.api.motor;

import net.minecraft.item.ItemStack;

public interface IMotorAction {

    public String getUnlocalizedName();

    public boolean isMoving();

    public ItemStack getIconStack();

}
