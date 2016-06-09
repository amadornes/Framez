package com.amadornes.framez.motor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amadornes.framez.api.motor.IMotorTrigger;

public class MotorTrigger {

    private final Map<IMotorTrigger, Boolean> triggers = new LinkedHashMap<IMotorTrigger, Boolean>();
    private EnumTriggerOperation operation;

    public MotorTrigger() {

        operation = EnumTriggerOperation.OR;
    }

    public MotorTrigger(IMotorTrigger trigger, boolean inverted) {

        this();
        addTrigger(trigger, inverted);
    }

    public MotorTrigger(MotorTrigger trigger) {

        this.triggers.putAll(trigger.triggers);
        this.operation = trigger.operation;
    }

    public Map<IMotorTrigger, Boolean> getTriggers() {

        return triggers;
    }

    public void addTrigger(IMotorTrigger trigger, boolean inverted) {

        triggers.put(trigger, inverted);
    }

    public void removeTrigger(IMotorTrigger trigger) {

        triggers.remove(trigger);
    }

    public EnumTriggerOperation getOperation() {

        return operation;
    }

    public void setOperation(EnumTriggerOperation operation) {

        this.operation = operation;
    }

    public boolean isActive() {

        return operation.isActive(this);
    }

    @Override
    public int hashCode() {

        return triggers.hashCode() * 31 + operation.hashCode();
    }

}
