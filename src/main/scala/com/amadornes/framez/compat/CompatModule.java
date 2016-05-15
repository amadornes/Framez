package com.amadornes.framez.compat;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.wrench.IFramezWrench;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
 * This file was originally created by me for use in BluePower but I thought it'd be quite useful to just copy it here instead of having
 * to rewrite it all over again, as it works perfectly as it is
 */
public abstract class CompatModule {

    public void prePreInit() {

    }

    public void preInit(FMLPreInitializationEvent ev) {

    }

    public void init(FMLInitializationEvent ev) {

    }

    public void postInit(FMLPostInitializationEvent ev) {

    }

    public void registerBlocks() {

    }

    public void registerItems() {

    }

    @SideOnly(Side.CLIENT)
    public void registerRenders() {

    }

    public IFramezWrench getWrench(ItemStack stack) {

        return null;
    }

}
