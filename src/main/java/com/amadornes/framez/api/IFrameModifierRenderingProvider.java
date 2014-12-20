package com.amadornes.framez.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IFrameModifierRenderingProvider extends IFrameModifierProvider {

    @SideOnly(Side.CLIENT)
    void renderFrame(IFrame frame);

}
