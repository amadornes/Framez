package com.amadornes.framez.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.block.BlockMoving;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.modifier.MotorModifierRegistry;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotorSlider;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezBlocks {

    public static Map<Block, String> motors = new HashMap<Block, String>();
    public static Block moving;

    public static void init() {

        for (List<IMotorModifier> l : MotorModifierRegistry.instance().getAllCombinations()) {
            String id;
            GameRegistry.registerTileEntity(MotorFactory.createMotorClass(TileMotorSlider.class, l),
                    id = MotorFactory.getIdentifier("slider", l));
            motors.put(new BlockMotor(id), id);
        }
        moving = new BlockMoving();
    }

    public static void register() {

        for (Entry<Block, String> e : motors.entrySet())
            GameRegistry.registerBlock(e.getKey(), References.Block.MOTOR + "_" + e.getValue());

        GameRegistry.registerBlock(moving, References.Block.MOVING);
        GameRegistry.registerTileEntity(TileMoving.class, References.Block.MOVING);
    }

}
