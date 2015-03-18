package com.amadornes.framez.api;

public class FramezAPI {

    private static IFramezAPI instance;

    public static IFramezAPI instance() {

        return instance;
    }

    public static void setup(IFramezAPI instance) {

        FramezAPI.instance = instance;
    }

}
