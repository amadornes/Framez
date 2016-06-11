package com.amadornes.framez.util;

import com.amadornes.framez.init.FramezItems;

import net.minecraft.item.ItemStack;

public enum EnumIconTypes {

    STOP,
    FORWARD,
    BACKWARD,
    CLOCKWISE,
    CCLOCKWISE,
    FORWARD_NB,
    BACKWARD_NB,
    CLOCKWISE_NB,
    CCLOCKWISE_NB,
    DOWN_NB;

    public static final EnumIconTypes[] VALUES = values();

    public ItemStack getIconStack() {

        return new ItemStack(FramezItems.icons, 1, ordinal());
    }

}
