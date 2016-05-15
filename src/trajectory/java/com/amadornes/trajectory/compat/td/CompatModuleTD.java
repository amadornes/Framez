package com.amadornes.trajectory.compat.td;

import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleTD extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        TrajectoryAPI.instance().registerBlockMovementHandler(new BlockMovementHandlerDuct());
    }
}
