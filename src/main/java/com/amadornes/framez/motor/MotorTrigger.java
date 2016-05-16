package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotorTrigger;

public class MotorTrigger {

    private final IMotorTrigger trigger;
    private final boolean inverted;

    public MotorTrigger(IMotorTrigger trigger, boolean inverted) {

        this.trigger = trigger;
        this.inverted = inverted;
    }

    public IMotorTrigger getTrigger() {

        return trigger;
    }

    public boolean isInverted() {

        return inverted;
    }

}
