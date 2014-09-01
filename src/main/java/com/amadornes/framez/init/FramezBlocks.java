package com.amadornes.framez.init;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MODID)
public final class FramezBlocks {

    public static final void init() {

        register();
    }

    private static final void register() {

        for (IMotorProvider m : FramezApi.inst().getMotorRegistry().getRegisteredMotors()) {
            GameRegistry.registerBlock(new BlockMotor(m), References.MOTOR_NAME + "." + m.getId());
            GameRegistry.registerTileEntity(m.getTileClass(), References.MOTOR_NAME + "." + m.getId());
        }
    }

}
