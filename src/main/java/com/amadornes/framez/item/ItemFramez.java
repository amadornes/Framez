package com.amadornes.framez.item;

import net.minecraft.item.Item;

public class ItemFramez extends Item implements IFramezItem {

    private final String name;

    public ItemFramez(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isBlock() {

        return false;
    }

}
