package com.amadornes.framez.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockFramez extends ItemBlock implements IFramezItem {

    private String name = "error";

    public ItemBlockFramez(Block block) {

        super(block);
    }

    public ItemBlockFramez(Block block, String name) {

        super(block);
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isBlock() {

        return true;
    }

}
