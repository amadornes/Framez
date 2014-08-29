package com.amadornes.framez;

import com.amadornes.framez.api.IFramezApi;
import com.amadornes.framez.api.IModifierRegistry;

public class FramezApiImpl implements IFramezApi {

    protected FramezApiImpl() {

    }

    @Override
    public IModifierRegistry getModifierRegistry() {

        return ModifierRegistry.INST;
    }

}
