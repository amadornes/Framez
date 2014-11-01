package com.amadornes.framez.compat.bc;

import buildcraft.api.statements.StatementManager;

import com.amadornes.framez.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleBC extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

        StatementManager.registerStatement(TriggerCanMove.INST);
        StatementManager.registerStatement(TriggerIsMoving.INST);
        StatementManager.registerTriggerProvider(new TriggerProvider());
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
