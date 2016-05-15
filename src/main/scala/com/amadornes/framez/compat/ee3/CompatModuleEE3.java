package com.amadornes.framez.compat.ee3;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleEE3 extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        if (!FramezCompatConfig.ee3_emc)
            return;

        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(FramezBlocks.metamorphic_stone, 1, 1), 195F);
        EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(FramezItems.iron_nugget, 1, 0), 256F / 9F);
    }

}
