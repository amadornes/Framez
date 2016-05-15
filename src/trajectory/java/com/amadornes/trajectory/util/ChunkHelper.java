package com.amadornes.trajectory.util;

import java.util.WeakHashMap;

import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import com.amadornes.trajectory.api.vec.BlockPos;

public class ChunkHelper {

    private static final WeakHashMap<World, PlayerManager> cache = new WeakHashMap<World, PlayerManager>();

    private static PlayerManager getPlayerManager(World world) {

        if (!cache.containsKey(world)) {
            if (!(world instanceof WorldServer)) {
                cache.put(world, null);
            } else
                cache.put(world, ((WorldServer) world).getPlayerManager());
        }

        return cache.get(world);
    }

    public static void updateChunk(Chunk chunk) {

        World world = chunk.worldObj;
        PlayerManager.PlayerInstance watcher = getChunkWatcher(chunk, world);
        if (watcher != null)
            watcher.sendChunkUpdate();
    }

    public static PlayerManager.PlayerInstance getChunkWatcher(Chunk chunk, World world) {

        PlayerManager playerManager = getPlayerManager(world);
        return playerManager != null ? playerManager.getOrCreateChunkWatcher(chunk.xPosition, chunk.zPosition, false) : null;
    }

    public static PlayerManager.PlayerInstance getChunkWatcher(World world, BlockPos pos) {

        return getChunkWatcher(world, pos.x, pos.z);
    }

    public static PlayerManager.PlayerInstance getChunkWatcher(World world, int x, int z) {

        PlayerManager playerManager = getPlayerManager(world);
        return playerManager != null ? playerManager.getOrCreateChunkWatcher(x >> 4, z >> 4, false) : null;
    }

}
