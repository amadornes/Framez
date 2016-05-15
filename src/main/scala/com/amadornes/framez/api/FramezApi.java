package com.amadornes.framez.api;

public class FramezApi {

    private static IFramezApi instance;

    public static IFramezApi instance() {

        return instance;
    }

    public static void setup(IFramezApi instance) {

        FramezApi.instance = instance;
    }

}