package com.amadornes.framez.api.frame;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISticky {

    @CapabilityInject(ISticky.class)
    public static final Capability<ISticky> CAPABILITY_STICKY = null;

    public boolean isSticky();

    public interface IAdvancedSticky extends ISticky {

        public int getStickiness();

    }

}
