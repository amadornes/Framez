package com.amadornes.framez.compat.fmp;

import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.CCRenderState;

public class RenderLightMatrix extends LightMatrix {

    public static final LightMatrix inst = new RenderLightMatrix();

    @Override
    public void operate() {

        CCRenderState.setBrightness(0xEE00EE);
    }

}
