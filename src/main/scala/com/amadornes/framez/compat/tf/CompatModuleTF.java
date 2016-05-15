package com.amadornes.framez.compat.tf;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.frame.FrameMaterial;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleTF extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (FramezCompatConfig.tf_invar_enabled)
            ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_INVAR,
                    FramezCompatConfig.tf_invar_max_moved_blocks, FramezCompatConfig.tf_invar_max_multiparts,
                    FramezCompatConfig.tf_invar_min_movement_time));
        if (FramezCompatConfig.tf_electrum_enabled)
            ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_ELECTRUM,
                    FramezCompatConfig.tf_electrum_max_moved_blocks, FramezCompatConfig.tf_electrum_max_multiparts,
                    FramezCompatConfig.tf_electrum_min_movement_time));
        if (FramezCompatConfig.tf_enderium_enabled)
            ModifierRegistry.instance.registerFrameMaterial(new FrameMaterial(References.Modifier.MATERIAL_ENDERIUM,
                    FramezCompatConfig.tf_enderium_max_moved_blocks, FramezCompatConfig.tf_enderium_max_multiparts,
                    FramezCompatConfig.tf_enderium_min_movement_time));
    }
}
