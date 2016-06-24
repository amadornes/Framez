package com.amadornes.framez.api.motor;

import net.minecraft.item.ItemStack;

public interface IMotorAction {

    public String getUnlocalizedName();

    public ItemStack getIconStack();

    public boolean isMoving();

    public boolean clashesWith(IMotorAction action);

    public int[] getCategories();

}
