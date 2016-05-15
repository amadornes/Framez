package com.amadornes.framez.compat.pc;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModulePC extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.pc_motor)
            ModifierRegistry.instance.registerMotorModifier(new MotorModifierPC());
    }
}
