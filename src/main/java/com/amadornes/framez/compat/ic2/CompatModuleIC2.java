package com.amadornes.framez.compat.ic2;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.modifier.MotorModifierRegistry;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleIC2 extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        MotorModifierRegistry.instance().registerModifier(new MotorModifierEU());
    }

}
