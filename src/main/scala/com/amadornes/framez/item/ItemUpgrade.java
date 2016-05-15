package com.amadornes.framez.item;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.ref.ModInfo;

public class ItemUpgrade extends ItemFramez {

    private String id;

    public ItemUpgrade(String id) {

        super("upgrade." + id);
        setCreativeTab(FramezCreativeTab.tab);
        setTextureName(ModInfo.MODID + ":upgrade/" + id);
        this.id = id;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "upgrade." + ModInfo.MODID + ":" + id;
    }

}
