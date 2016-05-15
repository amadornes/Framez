package com.amadornes.framez.client;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class GeneratedTexture extends TextureAtlasSprite {

    private BufferedImage img;
    private String animationFile;

    public GeneratedTexture(String id, BufferedImage img, String animationFile) {

        super(id);

        this.img = img;
        this.animationFile = animationFile;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {

        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {

        BufferedImage[] imgs = new BufferedImage[1 + Minecraft.getMinecraft().gameSettings.mipmapLevels];
        imgs[0] = img;
        AnimationMetadataSection animation = null;
        if (animationFile != null) {
            try {
                InputStream animationIS = getClass().getResourceAsStream(animationFile);
                if (animationIS != null) {
                    Gson gson = new Gson();
                    animation = gson.fromJson(
                            new JsonParser().parse(new InputStreamReader(animationIS)).getAsJsonObject().getAsJsonObject("animation"),
                            AnimationMetadataSection.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        loadSprite(imgs, animation, Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 0);
        return false;
    }
}