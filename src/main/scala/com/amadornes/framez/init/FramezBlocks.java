package com.amadornes.framez.init;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.block.BlockMetamorphicStone;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.block.BlockStopper;
import com.amadornes.framez.item.ItemBlockFrame;
import com.amadornes.framez.item.ItemBlockMetamorphicStone;
import com.amadornes.framez.item.ItemBlockMotor;
import com.amadornes.framez.item.ItemBlockStopper;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.modifier.MotorFactory;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileFrame;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.registry.GameRegistry;

public class FramezBlocks {

    public static Block metamorphic_stone;
    public static Block stopper;

    public static Map<String, Block> motors = new LinkedHashMap<String, Block>();
    public static Map<String, BlockFrame> frames = new LinkedHashMap<String, BlockFrame>();

    public static void init() {

        metamorphic_stone = new BlockMetamorphicStone();
        stopper = new BlockStopper();

        List<Class<? extends TileMotor>> motorTiles = new ArrayList<Class<? extends TileMotor>>();
        for (List<IMotorModifier> l : ModifierRegistry.instance.getAllMotorCombinations()) {
            String id = MotorFactory.getIdentifier(null, l);

            BlockMotor b = new BlockMotor(id);
            for (int i = 0; i < 16; i++) {
                if (b.tiles[i] == null)
                    continue;
                Class<? extends TileMotor> c = b.tiles[i].getClass();
                if (!motorTiles.contains(c)) {
                    motorTiles.add(c);
                    GameRegistry.registerTileEntity(c, b.tiles[i].getClass().getName());
                }
            }

            motors.put("motor_" + id, b);
        }

        for (IFrameMaterial m : ModifierRegistry.instance.frameMaterials) {
            frames.put(FrameFactory.getIdentifier("frame0", m), new BlockFrame(m, 0));
            frames.put(FrameFactory.getIdentifier("frame1", m), new BlockFrame(m, 1));
            frames.put(FrameFactory.getIdentifier("frame2", m), new BlockFrame(m, 2));
            frames.put(FrameFactory.getIdentifier("frame3", m), new BlockFrame(m, 3));
        }
    }

    public static void register() {

        GameRegistry.registerBlock(metamorphic_stone, ItemBlockMetamorphicStone.class, "metamorphic_stone");
        GameRegistry.registerBlock(stopper, ItemBlockStopper.class, "stopper");

        for (Entry<String, Block> e : motors.entrySet())
            GameRegistry.registerBlock(e.getValue(), ItemBlockMotor.class, e.getKey());

        for (Entry<String, BlockFrame> e : frames.entrySet())
            GameRegistry.registerBlock(e.getValue(), ItemBlockFrame.class, e.getKey());

        GameRegistry.registerTileEntity(TileFrame.class, ModInfo.MODID + ":frame");

    }

}
