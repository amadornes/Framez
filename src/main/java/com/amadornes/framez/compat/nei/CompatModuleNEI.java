package com.amadornes.framez.compat.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.init.FramezBlocks;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleNEI extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

        API.hideItem(new ItemStack(FramezBlocks.moving));
        API.addSubset("Frames", FramezApi.inst().getModifierRegistry().getAllPossibleCombinations());
    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerRenders() {

    }

}
