package com.amadornes.framez.part;

import net.minecraft.util.IStringSerializable;

public enum EnumFrameSideState implements IStringSerializable {

    NORMAL,
    PIPE,
    HOLLOW,
    PANEL;

    @Override
    public String getName() {

        return name();
    }

}
