package com.amadornes.framez.api.motor;

public final class MotorTriggerCategory {

    public static final MotorTriggerCategory CATEGORY_REDSTONE = new MotorTriggerCategory("redstone", "trigcat:framez.redstone");
    public static final MotorTriggerCategory CATEGORY_REDSTONE_BUNDLED = new MotorTriggerCategory("redstone.bundled",
            "trigcat:framez.redstone.bundled");

    private final String type, unlocalizedName;

    public MotorTriggerCategory(String type, String unlocalizedName) {

        this.type = type;
        this.unlocalizedName = unlocalizedName;
    }

    public String getType() {

        return type;
    }

    public String getUnlocalizedName() {

        return unlocalizedName;
    }

}
