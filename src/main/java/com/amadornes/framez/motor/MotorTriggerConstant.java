package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotorTrigger;

public class MotorTriggerConstant implements IMotorTrigger {

    private final boolean state;

    public MotorTriggerConstant(boolean state) {

        this.state = state;
    }

    @Override
    public String getUnlocalizedName() {

        return "framez:trigger." + (state ? "always" : "never");
    }

    @Override
    public boolean isActive() {

        return state;
    }

}
