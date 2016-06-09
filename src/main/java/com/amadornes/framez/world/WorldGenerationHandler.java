package com.amadornes.framez.world;

import java.util.Random;

import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezConfig;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenerationHandler implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        if (!FramezConfig.MetamorphicStone.generate) return;
        if (!world.provider.isSurfaceWorld()) return;
        if (random.nextDouble() > FramezConfig.MetamorphicStone.rarity) return;

        for (int i = 0; i < (FramezConfig.MetamorphicStone.minVeinsPerChunk
                + random.nextInt(FramezConfig.MetamorphicStone.maxVeinsPerChunk - FramezConfig.MetamorphicStone.minVeinsPerChunk)); i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = FramezConfig.MetamorphicStone.minGenHeight
                    + random.nextInt(FramezConfig.MetamorphicStone.maxGenHeight - FramezConfig.MetamorphicStone.minGenHeight);
            int z = chunkZ * 16 + random.nextInt(16);
            new WorldGenMinable(FramezBlocks.metamorphic_stone.getDefaultState(),
                    FramezConfig.MetamorphicStone.minVeinSize
                            + random.nextInt(FramezConfig.MetamorphicStone.maxVeinSize - FramezConfig.MetamorphicStone.minVeinSize),
                    b -> b.getBlock() == Blocks.STONE).generate(world, random, new BlockPos(x, y, z));
        }
    }
}