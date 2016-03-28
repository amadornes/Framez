package com.amadornes.framez.motor;

import com.amadornes.framez.api.motor.IMotorVariable;
import com.google.common.base.Function;

public class SimpleMotorVariable<T> implements IMotorVariable<T> {

    private final boolean display;
    private final String unlocalized;
    private final Function<T, String> toString;

    public SimpleMotorVariable() {

        this.display = false;
        this.unlocalized = null;
        this.toString = null;
    }

    public SimpleMotorVariable(String unlocalized, Function<T, String> toString) {

        this.display = true;
        this.unlocalized = unlocalized;
        this.toString = toString;
    }

    @Override
    public boolean shouldDisplayInOverview() {

        return display;
    }

    @Override
    public String getUnlocalizedName() {

        return unlocalized;
    }

    @Override
    public String valueToString(T value) {

        return toString.apply(value);
    }

}
