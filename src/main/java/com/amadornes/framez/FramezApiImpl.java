package com.amadornes.framez;

import com.amadornes.framez.api.IFramezApi;
import com.amadornes.framez.api.IModifierRegistry;
import com.amadornes.framez.api.IMotorRegistry;
import com.amadornes.framez.api.movement.IMovementApi;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementApi;

public class FramezApiImpl implements IFramezApi {

    protected FramezApiImpl() {

    }

    @Override
    public IModifierRegistry getModifierRegistry() {

        return ModifierRegistry.INST;
    }

    @Override
    public IMotorRegistry getMotorRegistry() {

        return MotorRegistry.INST;
    }

    @Override
    public IMovementApi getMovementApi() {

        return MovementApi.INST;
    }

}
