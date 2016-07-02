package com.amadornes.framez.motor.logic;

import com.amadornes.framez.motor.placement.IMotorPlacement;
import com.amadornes.framez.motor.placement.MotorPlacementDirectional;
import com.amadornes.framez.motor.placement.MotorPlacementFace;

public enum MotorLogicType {

    LINEAR_ACTUATOR(MotorLogicLinearActuator.class, new MotorPlacementFace(), "RenderLinearActuator", true),
    ROTATOR(MotorLogicRotator.class, new MotorPlacementFace(), "RenderRotator", true),
    SLIDER(MotorLogicSlider.class, new MotorPlacementDirectional(), "RenderSlider", false),
    BLINK_DRIVE(MotorLogicBlinkDrive.class, new MotorPlacementFace(), null, false);

    public static final MotorLogicType[] VALUES = values();

    public final Class<? extends IMotorLogic<?>> logicClass;
    public final IMotorPlacement<?> placement;
    public final String ftesr;
    public final boolean hasHead;

    private <T> MotorLogicType(Class<? extends IMotorLogic<T>> logicClass, IMotorPlacement<T> placement, String ftesr, boolean hasHead) {

        this.logicClass = logicClass;
        this.placement = placement;
        this.ftesr = ftesr;
        this.hasHead = hasHead;
    }

}
