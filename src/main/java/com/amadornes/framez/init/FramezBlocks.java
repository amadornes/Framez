package com.amadornes.framez.init;

import net.minecraft.block.Block;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.block.BlockMoving;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezBlocks {

    public static Block motor;
    public static Block moving;

    public static void init() {

        motor = new BlockMotor();
        moving = new BlockMoving();
    }

    public static void register() {

        GameRegistry.registerBlock(motor, References.Block.MOTOR);
        GameRegistry.registerTileEntity(TileMotorSlider.class, References.Block.MOTOR);

        GameRegistry.registerBlock(moving, References.Block.MOVING);
        GameRegistry.registerTileEntity(TileMoving.class, References.Block.MOVING);
    }

}
