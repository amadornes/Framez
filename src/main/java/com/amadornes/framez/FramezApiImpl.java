package com.amadornes.framez;

import com.amadornes.framez.api.IFramezAPI;
import com.amadornes.framez.api.modifier.IFrameModifierRegistry;
import com.amadornes.framez.api.modifier.IMotorModifierRegistry;
import com.amadornes.framez.api.movement.IFrameMovementRegistry;
import com.amadornes.framez.modifier.FrameModifierRegistry;
import com.amadornes.framez.modifier.MotorModifierRegistry;
import com.amadornes.framez.movement.FrameMovementRegistry;

public class FramezAPIImpl implements IFramezAPI {

    @Override
    public IFrameModifierRegistry frameModifiers() {

        return FrameModifierRegistry.instance();
    }

    @Override
    public IMotorModifierRegistry motorModifiers() {

        return MotorModifierRegistry.instance();
    }

    @Override
    public IFrameMovementRegistry movement() {

        return FrameMovementRegistry.instance();
    }

}
