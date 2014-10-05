package com.amadornes.framez.client;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconProvider {

    public static IIcon iconFrameBorder;
    public static IIcon iconFrameCross;
    public static IIcon iconFrameCrossBlocked;
    public static IIcon iconNothing;
    public static IIcon iconCrate;

    @SubscribeEvent
    public void onTextureRegister(TextureStitchEvent event) {

        if (event.map.getTextureType() == 0) {
            registerBlockTextures(event.map);
        } else {
            registerItemTextures(event.map);
        }

    }

    private void registerBlockTextures(IIconRegister reg) {

        iconFrameBorder = reg.registerIcon(ModInfo.MODID + ":frameBorder");
        iconFrameCross = reg.registerIcon(ModInfo.MODID + ":frameCross");
        iconFrameCrossBlocked = reg.registerIcon(ModInfo.MODID + ":frameCrossClosed");
        iconNothing = reg.registerIcon(ModInfo.MODID + ":nothing");
        iconCrate = reg.registerIcon(ModInfo.MODID + ":crate");

        for (IFrameModifierProvider p : FramezApi.inst().getModifierRegistry().getProviders())
            p.registerIcons(reg);
    }

    private void registerItemTextures(IIconRegister reg) {

    }

}
