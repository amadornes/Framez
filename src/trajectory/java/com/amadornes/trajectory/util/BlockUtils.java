package com.amadornes.trajectory.util;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class BlockUtils {

    public static void sneakySetBlock(World world, int x, int y, int z, Block block, int meta) {

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[y >> 4];
            if (ebs == null)
                chunk.getBlockStorageArray()[y >> 4] = ebs = new ExtendedBlockStorage(y >> 4 << 4, !world.provider.hasNoSky);
            ebs.setExtBlockMetadata(x & 15, y & 15, z & 15, meta);
            ebs.func_150818_a(x & 15, y & 15, z & 15, block);
            chunk.isModified = true;
        }
    }

    @SuppressWarnings("unchecked")
    public static void sneakySetTile(World world, int x, int y, int z, TileEntity te) {

        if (te == null) {
            sneakyRemoveTile(world, x, y, z);
            return;
        }

        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        if (chunk != null) {
            ChunkPosition pos = new ChunkPosition(x & 15, y, z & 15);
            if (chunk.chunkTileEntityMap.containsKey(pos))
                ((TileEntity) chunk.chunkTileEntityMap.get(pos)).invalidate();
            chunk.chunkTileEntityMap.put(pos, te);
            world.addTileEntity(te);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void sneakyRemoveTile(World world, int x, int y, int z, boolean removeFromChunk, boolean removeFromWorld) {

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

    public static void sneakyRemoveTile(World world, int x, int y, int z) {

        sneakyRemoveTile(world, x, y, z, true, true);
    }

}
