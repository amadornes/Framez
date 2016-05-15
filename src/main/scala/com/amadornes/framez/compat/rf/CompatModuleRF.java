package com.amadornes.framez.compat.rf;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleRF extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.rf_motor)
            ModifierRegistry.instance.registerMotorModifier(new MotorModifierRF());
    }

}
