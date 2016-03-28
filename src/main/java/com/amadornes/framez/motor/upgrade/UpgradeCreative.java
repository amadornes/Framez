package com.amadornes.framez.motor.upgrade;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorVariable;
import com.amadornes.framez.tile.TileMotor;

public class UpgradeCreative extends UpgradeBase {

    public UpgradeCreative(DynamicReference<? extends IMotor> motor, int slot, String type) {

        super(motor, slot, type);
    }

    @Override
    public int getAlterationPriority(IMotorVariable<?> variable) {

        if (variable == TileMotor.POWER_STORAGE_SIZE || variable == TileMotor.POWER_STORED) return Integer.MIN_VALUE;
        return super.getAlterationPriority(variable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T alterValue(T value, IMotorVariable<T> variable) {

        if (variable == TileMotor.POWER_STORAGE_SIZE || variable == TileMotor.POWER_STORED) return (T) (Double) 1000000.0D;
        return super.alterValue(value, variable);
    }

}
