package com.amadornes.framez.init;

import net.minecraft.block.Block;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.block.BlockMotorCore;
import com.amadornes.framez.block.BlockMoving;
import com.amadornes.framez.item.ItemBlockMotorCore;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MODID)
public final class FramezBlocks {

    public static Block moving;
    public static Block motorcore;

    public static final void init() {

        instantiate();

        register();
    }

    private static final void instantiate() {

        moving = new BlockMoving();
        motorcore = new BlockMotorCore();
    }

    private static final void register() {

        GameRegistry.registerBlock(motorcore, ItemBlockMotorCore.class, References.Names.Registry.MOTORCORE);

        for (IMotorProvider m : FramezApi.inst().getMotorRegistry().getRegisteredMotors()) {
            GameRegistry.registerBlock(new BlockMotor(m), References.Names.Registry.MOTOR + "." + m.getId());
            GameRegistry.registerTileEntity(m.getTileClass(), ModInfo.MODID + "." + References.Names.Registry.MOTOR + "." + m.getId());
        }

        GameRegistry.registerBlock(moving, References.Names.Registry.MOVING);
        GameRegistry.registerTileEntity(TileMoving.class, ModInfo.MODID + "." + References.Names.Registry.MOVING);
    }

}
