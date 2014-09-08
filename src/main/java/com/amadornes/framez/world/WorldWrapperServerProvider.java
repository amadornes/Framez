package com.amadornes.framez.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

public class WorldWrapperServerProvider {

    private static final List<WorldWrapperServer> worlds = new ArrayList<WorldWrapperServer>();

    public static WorldWrapperServer getWrapper(World realWorld) {

        WorldWrapperServer world = null;

        for (WorldWrapperServer w : worlds)
            if (w.getRealWorld().provider.dimensionId == realWorld.provider.dimensionId)
                return w;

        world = new WorldWrapperServer(realWorld);
        worlds.add(world);
        return world;
    }
}
