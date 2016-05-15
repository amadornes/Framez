package com.amadornes.framez.api.motor;

public enum EnumMotorAction {

    MOVE_FORWARD(true),
    MOVE_BACKWARD(true),
    STOP(false);

    public static final EnumMotorAction[] VALUES = values();

    private final boolean moving;

    private EnumMotorAction(boolean moving) {

        this.moving = moving;
    }

    public boolean isMoving() {

        return moving;
    }

}
