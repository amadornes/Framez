package com.amadornes.framez.compat.fmp;

import java.util.Map.Entry;

import net.minecraftforge.common.MinecraftForge;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroblockGenerator;
import codechicken.multipart.MultipartGenerator;

import com.amadornes.framez.CompatRegistryImpl;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.compat.CompatModule;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.modifier.ModifierRegistry;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatModuleFMP extends CompatModule {

    public static int traitIDMetamorphicStone, traitIDStopper, traitIDFrame;

    @Override
    public void prePreInit() {

        CompatRegistryImpl.compat_fmp = new FMPCompatRegistry();
        FramezApi.instance().compat().fmp().registerIgnoredCover("framez.stopper");
        FramezConfig.enable_frame_blocks = !(FramezCompatConfig.fmp_force_multipart_frames && FramezCompatConfig.fmp_frame_multipart);
    }

    @Override
    public void init(FMLInitializationEvent ev) {

        // FrameMovementRegistry.instance().registerBlockMovementHandler(new MovementHandlerFMP());
        // FrameMovementRegistry.instance().registerBlockMovementDataProvider(new MovementDataProviderFMP());

        if (FramezCompatConfig.fmp_frame_multipart)
            FramezApi.instance().compat().registerFramePlacementHandler(new FramePlacementHandlerFMP());
        if (FramezCompatConfig.fmp_microblock_frame_parts)
            ModifierRegistry.instance.registerFramePartHandler(new FramePartHandlerMicroblock());

        PartFactory.init();

        MinecraftForge.EVENT_BUS.register(new MOPHelper());

        if (FramezCompatConfig.fmp_metamorphic_stone_microblocks) {
            MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(FramezBlocks.metamorphic_stone, 0), "framez:metamorphic_stone");
            MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(FramezBlocks.metamorphic_stone, 1),
                    "framez:metamorphic_stone.cracked");
            MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(FramezBlocks.metamorphic_stone, 2),
                    "framez:metamorphic_stone.bricks");
            MicroMaterialRegistry.registerMaterial(new MicroMaterialMetamorphicStone(3), "framez:metamorphic_stone.water");
            MicroMaterialRegistry.registerMaterial(new MicroMaterialMetamorphicStone(4), "framez:metamorphic_stone.fire");
            MicroMaterialRegistry.registerMaterial(new MicroMaterialMetamorphicStone(5), "framez:metamorphic_stone.ice");
        }

        // MicroMaterialRegistry.registerMaterial(new MicroMaterialStopper(), "framez:stopper");

        traitIDMetamorphicStone = MicroblockGenerator.registerTrait("com.amadornes.framez.scalatraits.TMetamorphicStoneMicroblock");
        traitIDStopper = MicroblockGenerator.registerTrait("com.amadornes.framez.scalatraits.TStopperMicroblock");
        traitIDFrame = MicroblockGenerator.registerTrait("com.amadornes.framez.scalatraits.TFrameMicroblock");

        MultipartGenerator.registerTrait("com.amadornes.framez.api.movement.ISticky", "com.amadornes.framez.scalatraits.TStickyTile");
        MultipartGenerator.registerPassThroughInterface("com.amadornes.framez.api.movement.IFrame");
        MultipartGenerator.registerPassThroughInterface("com.amadornes.framez.api.movement.IModifiableFrame");

        if (FramezCompatConfig.fmp_microblock_frame_parts)
            for (Entry<String, BlockFrame> e : FramezBlocks.frames.entrySet())
                if (e.getValue().id == 0)
                    MicroMaterialRegistry.registerMaterial(new MicroMaterialFrame(e.getValue(), 0), e.getKey());

        // Register frame rotation plugin
        MinecraftForge.EVENT_BUS.register(new MovementPluginFMPFrame());
    }
}
