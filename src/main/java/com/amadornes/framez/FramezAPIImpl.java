package com.amadornes.framez;

import com.amadornes.framez.api.FramezAPI.IFramezAPI;
import com.amadornes.framez.api.frame.IFrameRegistry;
import com.amadornes.framez.api.motor.IMotorRegistry;
import com.amadornes.framez.api.movement.IMovementRegistry;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.motor.MotorRegistry;
import com.amadornes.framez.movement.MovementRegistry;

public class FramezAPIImpl implements IFramezAPI {

    @Override
    public IMotorRegistry getMotorRegistry() {

        return MotorRegistry.INSTANCE;
    }

    @Override
    public IFrameRegistry getFrameRegistry() {

        return FrameRegistry.INSTANCE;
    }

    @Override
    public IMovementRegistry getMovementRegistry() {

        return MovementRegistry.INSTANCE;
    }

}
