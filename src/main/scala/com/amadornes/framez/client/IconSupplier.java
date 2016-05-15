package com.amadornes.framez.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import codechicken.lib.render.TextureUtils;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconSupplier {

    public static List<FrameTexture> textures = new ArrayList<FrameTexture>();

    public static IIcon motor_border, motor_center;
    public static IIcon arrow_base;

    public static IIcon empty;

    public static IIcon sidemod_sticky;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        TextureMap map = event.map;

        if (map.getTextureType() == 0) {
            RenderHelper.instance().reloadModels();
            textures.clear();

            motor_border = map.registerIcon(ModInfo.MODID + ":motor/border");
            motor_center = map.registerIcon(ModInfo.MODID + ":motor/center");
            arrow_base = map.registerIcon(ModInfo.MODID + ":motor/arrow_base");

            sidemod_sticky = map.registerIcon(ModInfo.MODID + ":frame/modifier/sticky");

            empty = TextureUtils.getBlankIcon(16, map);

            for (IFrameMaterial m : ModifierRegistry.instance.frameMaterials)
                m.registerTextures();
            for (FrameTexture t : textures)
                t.registerIcons(map);
        }
    }

    @SubscribeEvent
    public void renderGame(RenderWorldLastEvent event) {

        Framez.proxy.setFrame(event.partialTicks);
    }

}
