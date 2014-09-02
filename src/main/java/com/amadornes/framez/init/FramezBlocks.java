package com.amadornes.framez.init;

import net.minecraft.block.Block;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.block.BlockMoving;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MODID)
public final class FramezBlocks {

    public static Block block_moving;

    public static final void init() {

        instantiate();

        register();
    }

    private static final void instantiate() {

        block_moving = new BlockMoving();
    }

    private static final void register() {

        for (IMotorProvider m : FramezApi.inst().getMotorRegistry().getRegisteredMotors()) {
            GameRegistry.registerBlock(new BlockMotor(m), References.MOTOR_NAME + "." + m.getId());
            GameRegistry.registerTileEntity(m.getTileClass(), References.MOTOR_NAME + "." + m.getId());
        }

        GameRegistry.registerBlock(block_moving, References.BLOCK_MOVING_NAME);
        GameRegistry.registerTileEntity(TileMoving.class, References.BLOCK_MOVING_NAME);
    }

}
