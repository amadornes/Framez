package com.amadornes.framez.api.frame;

public enum EnumFrameTexture {

    BORDER(EnumFramePart.BORDER),
    BORDER_PANEL(EnumFramePart.BORDER),
    CROSS(EnumFramePart.CROSS),
    CROSS_SMALL(EnumFramePart.CROSS),
    BINDING(EnumFramePart.BINDING);

    public static final EnumFrameTexture[] VALUES = values();

    private final EnumFramePart part;

    private EnumFrameTexture(EnumFramePart part) {

        this.part = part;
    }

    public EnumFramePart getPart() {

        return part;
    }

}
