package com.amadornes.framez.api.frame;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IFrame {

    @CapabilityInject(IFrame.class)
    public static final Capability<IFrame> CAPABILITY_FRAME = null;

    public IFrameMaterial getBorderMaterial();

    public IFrameMaterial getCrossMaterial();

    public IFrameMaterial getBindingMaterial();

}
