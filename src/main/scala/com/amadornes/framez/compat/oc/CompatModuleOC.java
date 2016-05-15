package com.amadornes.framez.compat.oc;

import li.cil.oc.api.Driver;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.compat.upgrade.ComputerizedUpgrade;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CompatModuleOC extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        if (FramezCompatConfig.oc_peripheral)
            Driver.add(new DriverMotor());

        if (FramezCompatConfig.oc_require_upgrade) {
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(GameRegistry.findBlock(Dependencies.OC, "case1")));
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(GameRegistry.findBlock(Dependencies.OC, "case2")));
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(GameRegistry.findBlock(Dependencies.OC, "case3")));
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(GameRegistry.findBlock(Dependencies.OC, "caseCreative")));
        }
    }

}
