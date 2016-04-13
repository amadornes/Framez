package com.amadornes.framez.api.motor;

import com.google.common.base.Function;

public interface IMotorRegistry {

    public void registerUpgrade(IMotorUpgradeFactory factory);

    public <T> IMotorVariable<T> createVariable();

    public <T> IMotorVariable<T> createVariable(String unlocalizedName, Function<T, String> toString);

    public IMotorVariable<Double> varPowerStored();

    public IMotorVariable<Double> varPowerStorageSize();

    public IMotorVariable<Double> varMovementTime();

}
