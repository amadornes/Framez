package com.amadornes.framez.client;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.amadornes.framez.Framez;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IconSupplier {

    public static IIcon wood_border;
    public static IIcon wood_border_panel;
    public static IIcon wood_cross;
    public static IIcon wood_simple;

    public static IIcon iron_border;
    public static IIcon iron_border_panel;
    public static IIcon iron_cross;
    public static IIcon iron_simple;

    public static IIcon copper_border;
    public static IIcon copper_border_panel;
    public static IIcon copper_cross;
    public static IIcon copper_simple;

    public static IIcon tin_border;
    public static IIcon tin_border_panel;
    public static IIcon tin_cross;
    public static IIcon tin_simple;

    public static IIcon silver_border;
    public static IIcon silver_border_panel;
    public static IIcon silver_cross;
    public static IIcon silver_simple;

    public static IIcon gold_border;
    public static IIcon gold_border_panel;
    public static IIcon gold_cross;
    public static IIcon gold_simple;

    public static IIcon bronze_border;
    public static IIcon bronze_border_panel;
    public static IIcon bronze_cross;
    public static IIcon bronze_simple;

    public static IIcon invar_border;
    public static IIcon invar_border_panel;
    public static IIcon invar_cross;
    public static IIcon invar_simple;

    public static IIcon electrum_border;
    public static IIcon electrum_border_panel;
    public static IIcon electrum_cross;
    public static IIcon electrum_simple;

    public static IIcon enderium_border;
    public static IIcon enderium_border_panel;
    public static IIcon enderium_cross;
    public static IIcon enderium_simple;

    public static IIcon motor_border;
    public static IIcon motor_center;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event) {

        TextureMap map = event.map;

        if (map.getTextureType() == 0) {
            wood_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_WOOD_BORDER);
            wood_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_WOOD_BORDER_PANEL);
            wood_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_WOOD_CROSS);
            wood_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_WOOD_SIMPLE);

            iron_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_IRON_BORDER);
            iron_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_IRON_BORDER_PANEL);
            iron_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_IRON_CROSS);
            iron_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_IRON_SIMPLE);

            copper_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_COPPER_BORDER);
            copper_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_COPPER_BORDER_PANEL);
            copper_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_COPPER_CROSS);
            copper_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_COPPER_SIMPLE);

            tin_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_TIN_BORDER);
            tin_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_TIN_BORDER_PANEL);
            tin_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_TIN_CROSS);
            tin_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_TIN_SIMPLE);

            silver_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_SILVER_BORDER);
            silver_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_SILVER_BORDER_PANEL);
            silver_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_SILVER_CROSS);
            silver_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_SILVER_SIMPLE);

            gold_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_GOLD_BORDER);
            gold_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_GOLD_BORDER_PANEL);
            gold_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_GOLD_CROSS);
            gold_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_GOLD_SIMPLE);

            bronze_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_BRONZE_BORDER);
            bronze_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_BRONZE_BORDER_PANEL);
            bronze_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_BRONZE_CROSS);
            bronze_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_BRONZE_SIMPLE);

            invar_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_INVAR_BORDER);
            invar_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_INVAR_BORDER_PANEL);
            invar_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_INVAR_CROSS);
            invar_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_INVAR_SIMPLE);

            electrum_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ELECTRUM_BORDER);
            electrum_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ELECTRUM_BORDER_PANEL);
            electrum_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ELECTRUM_CROSS);
            electrum_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ELECTRUM_SIMPLE);

            enderium_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ENDERIUM_BORDER);
            enderium_border_panel = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ENDERIUM_BORDER_PANEL);
            enderium_cross = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ENDERIUM_CROSS);
            enderium_simple = map.registerIcon(ModInfo.MODID + ":" + References.Texture.FRAME_ENDERIUM_SIMPLE);

            motor_border = map.registerIcon(ModInfo.MODID + ":" + References.Texture.MOTOR_BORDER);
            motor_center = map.registerIcon(ModInfo.MODID + ":" + References.Texture.MOTOR_CENTER);
        }
    }

    @SubscribeEvent
    public void renderGame(net.minecraftforge.client.event.RenderWorldLastEvent event) {

        Framez.proxy.setFrame(event.partialTicks);
    }

}
