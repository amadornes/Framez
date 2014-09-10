package com.amadornes.framez.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import codechicken.multipart.TileMultipart;

public class BlockUtils {

    public static void removeTileEntity(World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null)
            return;

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ChunkPosition chunkposition = new ChunkPosition(x & 15, y, z & 15);

            if (chunk.isChunkLoaded) {
                chunk.chunkTileEntityMap.remove(chunkposition);
                if (!(te instanceof TileMultipart)) {
                    try {
                        world.loadedTileEntityList.remove(te);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

}
