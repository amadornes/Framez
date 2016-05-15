package com.amadornes.trajectory.compat.cc;

import com.amadornes.trajectory.ModInfo;
import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;

public class CompatModuleCC extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-Computer");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-Turtle");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-TurtleExpanded");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-TurtleAdvanced");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-command_computer");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-Peripheral");
        FMLInterModComms.sendMessage(ModInfo.MODID, "no_invalidate_block", "ComputerCraft:CC-Cable");
    }
}
