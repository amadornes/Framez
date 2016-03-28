package com.amadornes.framez.init;

import com.amadornes.framez.part.PartFrame;

import mcmultipart.multipart.MultipartRegistry;

public class FramezParts {

    public static void register() {

        MultipartRegistry.registerPart(PartFrame.class, "frame");
    }

}
