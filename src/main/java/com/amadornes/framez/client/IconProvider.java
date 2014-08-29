package com.amadornes.framez.client;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconProvider {

    public static IIcon iconFrameBorder;
    public static IIcon iconFrameCross;
    public static IIcon iconNothing;

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
        iconNothing = reg.registerIcon(ModInfo.MODID + ":nothing");

        for (IFrameModifierProvider p : FramezApi.inst().getModifierRegistry().getProviders())
            p.registerIcons(reg);
    }

    private void registerItemTextures(IIconRegister reg) {

    }

}
