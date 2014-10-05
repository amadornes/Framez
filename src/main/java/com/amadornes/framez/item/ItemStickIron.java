package com.amadornes.framez.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStickIron extends Item {

    public ItemStickIron() {

        setCreativeTab(CreativeTabFramez.inst);

        setUnlocalizedName(References.Names.Unlocalized.IRON_STICK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        itemIcon = reg.registerIcon(References.Textures.IRON_STICK);
    }

}
