package com.amadornes.framez.motor;

import java.util.Map.Entry;

import com.amadornes.framez.api.motor.IMotorTrigger;

public enum EnumTriggerOperation {

    OR {

        @Override
        public boolean isActive(MotorTrigger trigger) {

            for (Entry<IMotorTrigger, Boolean> e : trigger.getTriggers().entrySet()) {
                if (e.getKey().isActive() != e.getValue()) {
                    return true;
                }
            }
            return false;
        }
    },
    AND {

        @Override
        public boolean isActive(MotorTrigger trigger) {

            if (trigger.getTriggers().isEmpty()) {
                return false;
            }
            for (Entry<IMotorTrigger, Boolean> e : trigger.getTriggers().entrySet()) {
                if (e.getKey().isActive() == e.getValue()) {
                    return false;
                }
            }
            return true;
        }
    },
    XOR {

        @Override
        public boolean isActive(MotorTrigger trigger) {

            boolean state = false;
            for (Entry<IMotorTrigger, Boolean> e : trigger.getTriggers().entrySet()) {
                if (e.getKey().isActive() != e.getValue()) {
                    state = !state;
                }
            }
            return state;
        }
    };

    public abstract boolean isActive(MotorTrigger trigger);

}
