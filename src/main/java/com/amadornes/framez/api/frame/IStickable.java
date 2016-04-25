package com.amadornes.framez.api.frame;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IStickable {

    @CapabilityInject(IStickable.class)
    public static final Capability<IStickable> CAPABILITY_STICKABLE = null;

    public boolean canStickTo();

    public interface IAdvancedStickable extends ISticky {

        public int getRequiredStickiness();

    }

}
