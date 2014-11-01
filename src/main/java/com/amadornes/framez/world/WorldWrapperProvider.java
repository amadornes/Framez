package com.amadornes.framez.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class WorldWrapperProvider {

    private static final List<WorldWrapperServer> worlds = new ArrayList<WorldWrapperServer>();

    public static WorldWrapperServer getWrapper(World realWorld) {

        return getWrapper(realWorld.provider.dimensionId);
    }

    public static WorldWrapperServer getWrapper(int dimId) {

        WorldWrapperServer world = null;

        for (WorldWrapperServer w : worlds)
            if (w.getRealWorld().provider.dimensionId == dimId)
                return w;

        world = new WorldWrapperServer(MinecraftServer.getServer().worldServerForDimension(dimId));
        worlds.add(world);
        return world;
    }
}
