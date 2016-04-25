package com.amadornes.framez.api.frame;

public enum EnumFramePart {

    BORDER(1.0F),
    CROSS(0.7F),
    BINDING(0.3F);

    private final float multiplier;

    private EnumFramePart(float multiplier) {

        this.multiplier = multiplier;
    }

    public float getDefaultWeightMultiplier() {

        return multiplier;
    }

}
