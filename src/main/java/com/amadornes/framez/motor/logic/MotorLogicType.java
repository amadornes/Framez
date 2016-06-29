package com.amadornes.framez.motor.logic;

public enum MotorLogicType {

    LINEAR_ACTUATOR(MotorLogicLinearActuator.class, "RenderLinearActuator", true),
    ROTATOR(MotorLogicRotator.class, "RenderRotator", true),
    SLIDER(MotorLogicSlider.class, "RenderSlider", false),
    BLINK_DRIVE(MotorLogicBlinkDrive.class, null, false);

    public static final MotorLogicType[] VALUES = values();

    public final Class<? extends IMotorLogic> logicClass;
    public final String ftesr;
    public final boolean hasHead;

    private MotorLogicType(Class<? extends IMotorLogic> logicClass, String ftesr, boolean hasHead) {
        this.logicClass = logicClass;
        this.ftesr = ftesr;
        this.hasHead = hasHead;
    }

}
