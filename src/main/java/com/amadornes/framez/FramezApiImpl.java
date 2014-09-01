package com.amadornes.framez;

import com.amadornes.framez.api.IFramezApi;
import com.amadornes.framez.api.IModifierRegistry;
import com.amadornes.framez.api.IMotorRegistry;

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

}
