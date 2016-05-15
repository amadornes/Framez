package com.amadornes.framez.compat.fmp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.trajectory.api.vec.BlockPos;

public class PartFactory implements IPartFactory, IPartConverter {

    public static final PartFactory inst = new PartFactory();

    public static void init() {

        List<String> parts = new ArrayList<String>();

        for (IFrameMaterial m : ModifierRegistry.instance.frameMaterials)
            parts.add(FrameFactory.getIdentifier(ModInfo.MODID + ":frame", m));
        parts.add("framez.frame");

        MultiPartRegistry.registerParts(inst, parts.toArray(new String[parts.size()]));
        MultiPartRegistry.registerConverter(inst);
    }

    @Override
    public TMultiPart createPart(String part, boolean client) {

        if (part.equals("framez.frame"))
            return FrameFactory
                    .createFrame(PartFrame.class, ModifierRegistry.instance.findFrameMaterial(References.Modifier.MATERIAL_WOOD));
        if (part.startsWith(ModInfo.MODID + ":frame"))
            return FrameFactory.createFrame(PartFrame.class, FrameFactory.getMaterial(part));
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Iterable<Block> blockTypes() {

        return (Collection) FramezBlocks.frames.values();
    }

    @Override
    public TMultiPart convert(World world, BlockCoord loc) {

        BlockPos position = new BlockPos(loc.x, loc.y, loc.z);
        IFrame frame = MovementRegistry.instance.getFrameAt(world, position);
        PartFrame newFrame = FrameFactory.createFrame(PartFrame.class, frame.getMaterial());
        newFrame.cloneFrame(frame);
        return newFrame;
    }
}
