package com.amadornes.framez.compat.ae2;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.modifier.MotorModifierRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleAE2 extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        MotorModifierRegistry.instance().registerModifier(new MotorModifierAE2());
    }

}
