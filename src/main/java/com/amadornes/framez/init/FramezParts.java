package com.amadornes.framez.init;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.part.PartFrame;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.util.ResourceLocation;

public class FramezParts {

    public static void register() {

        MultipartRegistry.registerPart(PartFrame.class, new ResourceLocation(ModInfo.MODID, "frame"));
    }

}
