package com.amadornes.framez.compat.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.api.API;

import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleNEI extends CompatModule {

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            postInitClient();
    }

    @SideOnly(Side.CLIENT)
    private void postInitClient() {

        if (FramezCompatConfig.nei_subsets) {
            List<ItemStack> frames = new ArrayList<ItemStack>();
            for (BlockFrame b : FramezBlocks.frames.values())
                if (b.id == 0)
                    frames.add(new ItemStack(b));
            API.addSubset(ModInfo.NAME + ".Frames", frames);

            List<ItemStack> motors = new ArrayList<ItemStack>();
            List<ItemStack> sliders = new ArrayList<ItemStack>();
            List<ItemStack> rotators = new ArrayList<ItemStack>();
            for (Block b : FramezBlocks.motors.values()) {
                motors.add(new ItemStack(b, 1, 0));
                motors.add(new ItemStack(b, 1, 1));

                sliders.add(new ItemStack(b, 1, 0));
                rotators.add(new ItemStack(b, 1, 1));
            }
            API.addSubset(ModInfo.NAME + ".Motors", motors);
            API.addSubset(ModInfo.NAME + ".Motors." + EnumChatFormatting.YELLOW + "Sliders", sliders);
            API.addSubset(ModInfo.NAME + ".Motors." + EnumChatFormatting.YELLOW + "Rotators", rotators);
        }

        if (FramezCompatConfig.nei_hide_items)
            for (BlockFrame b : FramezBlocks.frames.values())
                if (b.id != 0)
                    API.hideItem(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE));
    }
}
