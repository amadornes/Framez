package com.amadornes.framez.compat.oc;

import li.cil.oc.api.Driver;

import com.amadornes.framez.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleOC extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

        Driver.add(new DriverMotor());
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerRenders() {

    }

}
