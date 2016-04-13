package com.amadornes.framez.api.frame;

public enum EnumFramePart {

    BORDER(1.0),
    CROSS(0.7),
    BINDING(0.3);

    private final double multiplier;

    private EnumFramePart(double multiplier) {

        this.multiplier = multiplier;
    }

    public double getDefaultWeightMultiplier() {

        return multiplier;
    }

}
