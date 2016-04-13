package com.amadornes.framez.api.motor;

public enum EnumMotorStatus {

    MOVING,
    CAN_MOVE,
    BLOCKED,
    STOPPED;

    public static final EnumMotorStatus[] VALUES = values();

}
