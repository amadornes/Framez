package com.amadornes.trajectory.compat.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;

import com.amadornes.trajectory.Trajectory;
import com.amadornes.trajectory.compat.CompatModule;

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

        API.hideItem(new ItemStack(Trajectory.moving));
    }
}
