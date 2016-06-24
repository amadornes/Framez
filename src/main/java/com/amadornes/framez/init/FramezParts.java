package com.amadornes.framez.init;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.block.BlockMetamorphicStone;
import com.amadornes.framez.part.MicroDelegateMetamorphicStone;
import com.amadornes.framez.part.PartFrame;

import mcmultipart.microblock.BlockMicroMaterial;
import mcmultipart.microblock.MicroblockRegistry;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.util.ResourceLocation;

public class FramezParts {

    public static void register() {

        MultipartRegistry.registerPart(PartFrame.class, new ResourceLocation(ModInfo.MODID, "frame"));

        for (BlockMetamorphicStone.Type type : BlockMetamorphicStone.Type.VALUES)
            registerMetamorphicStoneMicroMaterial(type);
    }

    private static void registerMetamorphicStoneMicroMaterial(BlockMetamorphicStone.Type type) {

        BlockMicroMaterial bmm = new BlockMicroMaterial(
                FramezBlocks.metamorphic_stone.getDefaultState().withProperty(BlockMetamorphicStone.TYPE, type));
        if (type.hasLogic) bmm = bmm.withDelegate(t -> new MicroDelegateMetamorphicStone(t.getFirst(), type));
        MicroblockRegistry.registerMaterial(bmm);
    }

}
