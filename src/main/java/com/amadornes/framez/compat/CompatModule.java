package com.amadornes.framez.compat;

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

    public abstract void preInit(FMLPreInitializationEvent ev);

    public abstract void init(FMLInitializationEvent ev);

    public abstract void postInit(FMLPostInitializationEvent ev);

    public abstract void registerBlocks();

    public abstract void registerItems();

    @SideOnly(Side.CLIENT)
    public abstract void registerRenders();

}
