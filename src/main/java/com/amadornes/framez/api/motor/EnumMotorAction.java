package com.amadornes.framez.api.motor;

import com.amadornes.framez.ModInfo;

public enum EnumMotorAction implements IMotorAction {

    MOVE_FORWARD("forward", true),
    MOVE_BACKWARD("backward", true),
    STOP("stop", false);

    public static final EnumMotorAction[] VALUES = values();

    private final String name;
    private final boolean moving;

    private EnumMotorAction(String name, boolean moving) {

        this.name = name;
        this.moving = moving;
    }

    @Override
    public String getUnlocalizedName() {

        return "gui." + ModInfo.MODID + ":motor.action." + name;
    }

    @Override
    public boolean isMoving() {

        return moving;
    }

}
