package com.amadornes.framez.motor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amadornes.framez.api.motor.IMotorRegistry;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.api.motor.IMotorVariable;
import com.amadornes.framez.motor.upgrade.UpgradeFactoryBase;
import com.amadornes.framez.tile.TileMotor;
import com.google.common.base.Function;

public enum MotorRegistry implements IMotorRegistry {

    INSTANCE;

    public Map<String, IMotorUpgradeFactory> upgrades = new LinkedHashMap<String, IMotorUpgradeFactory>();
    public Map<String, IMotorUpgradeFactory> internalUpgrades = new LinkedHashMap<String, IMotorUpgradeFactory>();

    @Override
    public void registerUpgrade(IMotorUpgradeFactory factory) {

        upgrades.put(factory.getType(), factory);
    }

    public void registerUpgradeInternal(UpgradeFactoryBase factory) {

        registerUpgrade(factory);
        internalUpgrades.put(factory.getType(), factory);
    }

    @Override
    public <T> IMotorVariable<T> createVariable() {

        return new SimpleMotorVariable<T>();
    }

    @Override
    public <T> IMotorVariable<T> createVariable(String unlocalizedName, Function<T, String> toString) {

        return new SimpleMotorVariable<T>(unlocalizedName, toString);
    }

    @Override
    public IMotorVariable<Double> varPowerStored() {

        return TileMotor.POWER_STORED;
    }

    @Override
    public IMotorVariable<Double> varPowerStorageSize() {

        return TileMotor.POWER_STORAGE_SIZE;
    }

    @Override
    public IMotorVariable<Double> varMovementTime() {

        return TileMotor.MOVEMENT_TIME;
    }

}
