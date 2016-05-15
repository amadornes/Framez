package com.amadornes.framez.compat;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.CompatRegistryImpl;
import com.amadornes.framez.api.wrench.IFramezWrench;

public class CompatRegistryAdvImpl extends CompatRegistryImpl {

    @Override
    public IFramezWrench getModdedWrench(ItemStack stack) {

        return CompatibilityUtils.getWrenchFromStack(stack);
    }

}
