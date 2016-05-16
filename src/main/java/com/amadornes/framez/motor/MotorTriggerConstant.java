package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotorTrigger;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class MotorTriggerConstant implements IMotorTrigger {

    public MotorTriggerConstant() {
    }

    @Override
    public String getUnlocalizedName(boolean inverted) {

        return "trigger.framez:" + (inverted ? "never" : "always");
    }

    @Override
    public boolean isActive() {

        return true;
    }

    @Override
    public boolean canBeInverted() {

        return true;
    }

    @Override
    public ItemStack getIconStack() {

        return new ItemStack(Blocks.REDSTONE_BLOCK);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        return obj == this || obj instanceof MotorTriggerConstant;
    }

}
