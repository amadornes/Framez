package com.amadornes.framez.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIngotSpecoriumLove extends ItemFramez {

    public ItemIngotSpecoriumLove() {

        super("specorium_love");

        setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        itemIcon = reg.registerIcon(ModInfo.MODID + ":ingot_specorium_love");
    }

}
