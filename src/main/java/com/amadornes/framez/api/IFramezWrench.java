package com.amadornes.framez.api;

import net.minecraft.item.ItemStack;

public interface IFramezWrench {

    public WrenchAction getWrenchAction(ItemStack stack);

    public static enum WrenchAction {
        ROTATE, DEBUG, CONFIG;
    }

}
