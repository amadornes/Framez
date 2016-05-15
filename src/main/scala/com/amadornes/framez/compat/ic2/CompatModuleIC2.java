package com.amadornes.framez.compat.ic2;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleIC2 extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.ic2_motor)
            ModifierRegistry.instance.registerMotorModifier(new MotorModifierEU());
    }

}
