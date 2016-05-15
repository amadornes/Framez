package com.amadornes.framez.util;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.amadornes.framez.init.FramezBlocks;

import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerationHandler implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (!world.provider.isSurfaceWorld())
            return;

        if (random.nextInt(17) < 7)
            return;

        for (int i = 0; i < random.nextInt(3); i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(100);
            int z = chunkZ * 16 + random.nextInt(16);
            // new WorldGenBigVein(FramezBlocks.metamorphic_stone, 0, 7, 15, 1, 6, 2, 5).generate(world, random, x, y, z);
            new WorldGenMinable(FramezBlocks.metamorphic_stone, 0, 30 + random.nextInt(20), Blocks.stone).generate(world, random, x, y, z);
        }
    }
}
