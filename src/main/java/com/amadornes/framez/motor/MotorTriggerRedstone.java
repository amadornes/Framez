package com.amadornes.framez.motor;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorTrigger;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MotorTriggerRedstone implements IMotorTrigger {

    private final DynamicReference<? extends IMotor> motor;

    public MotorTriggerRedstone(DynamicReference<? extends IMotor> motor) {

        this.motor = motor;
    }

    @Override
    public String getUnlocalizedName(boolean inverted) {

        return "trigger.framez:redstone" + (inverted ? ".inverted" : "");
    }

    @Override
    public boolean isActive() {

        return motor.get().getMotorWorld().isBlockPowered(motor.get().getMotorPos());
    }

    @Override
    public boolean canBeInverted() {

        return true;
    }

    @Override
    public ItemStack getIconStack() {

        return new ItemStack(Items.REDSTONE);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        return obj == this || obj instanceof MotorTriggerRedstone;
    }

}
