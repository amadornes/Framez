package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotorTrigger;
import com.amadornes.framez.api.motor.MotorTriggerType;

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
    public ItemStack getIconStack(boolean inverted) {

        return new ItemStack(inverted ? Blocks.BARRIER : Blocks.REDSTONE_BLOCK);
    }

    @Override
    public MotorTriggerType getTriggerType() {

        return MotorTriggerType.TYPE_CONSTANT;
    }

    @Override
    public boolean isActive() {

        return true;
    }

    @Override
    public boolean canBeInverted() {

        return false;
    }

    @Override
    public boolean requiresInvertedOverlay() {

        return false;
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
