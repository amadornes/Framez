package com.amadornes.framez.init;

import java.util.function.IntFunction;

import com.amadornes.framez.Framez;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.block.BlockBreaker;
import com.amadornes.framez.block.BlockMetamorphicStone;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.item.ItemBlockMetamorphicStone;
import com.amadornes.framez.item.ItemBlockMotor;
import com.amadornes.framez.motor.logic.MotorLogicType;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FramezBlocks {

    public static Block motor;
    public static Block metamorphic_stone;
    public static Block block_breaker;

    public static void initialize() {

        motor = new BlockMotor();
        metamorphic_stone = new BlockMetamorphicStone();
        block_breaker = new BlockBreaker();
    }

    public static void register() {

        registerBlock(motor, ItemBlockMotor.class, "motor", MotorLogicType.VALUES.length);
        GameRegistry.registerTileEntity(TileMotor.class, ModInfo.MODID + ":motor");
        registerBlock(metamorphic_stone, ItemBlockMetamorphicStone.class, "metamorphic_stone", 6);
        registerBlock(block_breaker, "block_breaker", 1);
    }

    private static void registerBlock(Block block, String name, int variants) {

        registerBlock(block, ItemBlock.class, name, variants);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemblock, String name, int variants) {

        int[] vars = new int[variants];
        for (int i = 0; i < variants; vars[i] = i++);
        registerBlock(block, itemblock, name, i -> i + "", vars);
    }

    @SuppressWarnings("deprecation") // TODO: Upgrade to the new registries
    private static void registerBlock(Block block, Class<? extends ItemBlock> itemblock, String name, IntFunction<String> i2s,
            int... variants) {

        GameRegistry.registerBlock(block, itemblock, name);
        Item item = Item.getItemFromBlock(block);
        if (variants.length <= 1) {
            Framez.proxy.registerItemRenderer(item, name, i2s, -1);
        } else {
            for (int v : variants)
                Framez.proxy.registerItemRenderer(item, name, i2s, v);
        }
        block.setCreativeTab(FramezCreativeTab.tab);
    }

}
