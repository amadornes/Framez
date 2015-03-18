package com.amadornes.framez.compat.rf;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.modifier.MotorModifierRegistry;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleRF extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MotorModifierRegistry.instance().registerModifier(new MotorModifierRF());
    }

}
