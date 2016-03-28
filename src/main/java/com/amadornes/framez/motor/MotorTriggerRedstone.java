package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorTrigger;

public class MotorTriggerRedstone implements IMotorTrigger {

    private final IMotor motor;
    private final boolean inverted;

    public MotorTriggerRedstone(IMotor motor, boolean inverted) {

        this.motor = motor;
        this.inverted = inverted;
    }

    @Override
    public String getUnlocalizedName() {

        return "framez:trigger.redstone" + (inverted ? ".inverted" : "");
    }

    @Override
    public boolean isActive() {

        return motor.getMotorWorld().isBlockPowered(motor.getMotorPos()) ? !inverted : inverted;
    }

}
