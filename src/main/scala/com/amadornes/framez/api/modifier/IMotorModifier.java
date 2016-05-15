package com.amadornes.framez.api.modifier;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.modifier.IModifier.IModifierTrait;
import com.amadornes.framez.api.movement.IMotor;

public interface IMotorModifier extends IModifierTrait<IMotorModifier, IMotor> {

    public void registerRecipes(ItemStack unmodified, ItemStack modified);

    public static interface IMotorModifierPower extends IMotorModifier {

    }
}