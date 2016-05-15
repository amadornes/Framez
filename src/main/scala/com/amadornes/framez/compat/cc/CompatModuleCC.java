package com.amadornes.framez.compat.cc;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.compat.upgrade.ComputerizedUpgrade;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;

public class CompatModuleCC extends CompatModule {

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

        if (FramezCompatConfig.cc_peripheral)
            ComputerCraftAPI.registerPeripheralProvider(new CCPeripheralProvider());

        if (FramezCompatConfig.cc_require_upgrade) {
            Block b = GameRegistry.findBlock(Dependencies.CC, "CC-Computer");
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(b, 1, 0));
            ComputerizedUpgrade.upgrade.items.add(new ItemStack(b, 1, 16384));
        }
    }

}
