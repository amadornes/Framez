package com.amadornes.trajectory.compat.fmp;

import net.minecraftforge.common.MinecraftForge;

import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.compat.CompatModule;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleFMP extends CompatModule {

    @Override
    public void init(FMLInitializationEvent ev) {

        BlockMovementHandlerMultipart movementHandler = new BlockMovementHandlerMultipart();
        TrajectoryAPI.instance().registerBlockMovementHandler(movementHandler);
        MinecraftForge.EVENT_BUS.register(movementHandler);
        MinecraftForge.EVENT_BUS.register(new MicroblockMovementPlugin());
        MinecraftForge.EVENT_BUS.register(new McMultipartMovementPlugin());
    }
}
