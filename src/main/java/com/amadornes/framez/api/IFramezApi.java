package com.amadornes.framez.api;

import com.amadornes.framez.api.modifier.IFrameModifierRegistry;
import com.amadornes.framez.api.modifier.IMotorModifierRegistry;
import com.amadornes.framez.api.movement.IFrameMovementRegistry;

public interface IFramezAPI {

    public IFrameModifierRegistry frameModifiers();

    public IMotorModifierRegistry motorModifiers();

    public IFrameMovementRegistry movement();

}
