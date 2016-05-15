package com.amadornes.framez.api.movement;

public class FrameFeature {

    public static final FrameFeature BLOCK_SIDES = new FrameFeature("block_sides");
    public static final FrameFeature HIDE_SIDES = new FrameFeature("hide_sides");

    public final String id;

    public FrameFeature(String id) {

        this.id = id;
    }

}
