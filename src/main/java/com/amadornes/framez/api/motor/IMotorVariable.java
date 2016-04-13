package com.amadornes.framez.api.motor;

public interface IMotorVariable<T> {

    public boolean shouldDisplayInOverview();

    public String getUnlocalizedName();

    public String valueToString(T value);

}
