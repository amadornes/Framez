package com.amadornes.framez.api;

import cpw.mods.fml.common.Loader;

public class FramezApi {

    private static IFramezApi inst;

    public static IFramezApi inst() {

        return inst;
    }

    public static void init(IFramezApi inst) {

        if (!Loader.instance().activeModContainer().getModId().equals("framez"))
            return;

        FramezApi.inst = inst;
    }

}
