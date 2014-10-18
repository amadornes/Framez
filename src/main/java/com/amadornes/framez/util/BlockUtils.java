package com.amadornes.framez.util;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class BlockUtils {

    // public static final List<TileEntity> removedTEs = new ArrayList<TileEntity>();
    // public static final List<TileEntity> addedTEs = new ArrayList<TileEntity>();

    @SuppressWarnings("rawtypes")
    public static void removeTileEntity(World world, int x, int y, int z, boolean removeFromChunk, boolean removeFromWorld) {

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        TileEntity te = null;
        if (chunk != null) {
            if (removeFromChunk) {
                te = (TileEntity) chunk.chunkTileEntityMap.remove(new ChunkPosition(x & 15, y, z & 15));
            } else {
                te = (TileEntity) chunk.chunkTileEntityMap.get(new ChunkPosition(x & 15, y, z & 15));
            }
        }

        if (te != null && removeFromWorld) {
            Iterator it = world.loadedTileEntityList.iterator();
            while (it.hasNext()) {
                TileEntity tile = (TileEntity) it.next();
                if (tile == te)
                    it.remove();
            }
        }
    }

    public static void removeTileEntity(World world, int x, int y, int z) {

        removeTileEntity(world, x, y, z, true, true);
    }

    @SuppressWarnings("unchecked")
    public static void setTileEntity(World world, int x, int y, int z, TileEntity te) {

        if (te == null)
            return;

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ChunkPosition pos = new ChunkPosition(x & 15, y, z & 15);
            if (chunk.chunkTileEntityMap.containsKey(pos))
                ((TileEntity) chunk.chunkTileEntityMap.get(pos)).invalidate();
            chunk.chunkTileEntityMap.put(pos, te);
        }
    }

    public static void setBlockSneaky(World world, int x, int y, int z, Block block) {

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[y >> 4];
            if (ebs != null)
                ebs.func_150818_a(x & 15, y & 15, z & 15, block);
        }
    }

    public static void setBlockMetadataSneaky(World world, int x, int y, int z, int meta) {

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[y >> 4];
            if (ebs == null) {
                ebs = new ExtendedBlockStorage(y >> 4, true);
                chunk.getBlockStorageArray()[y >> 4] = ebs;
            }
            ebs.setExtBlockMetadata(x & 15, y & 15, z & 15, meta);
        }
    }
}
