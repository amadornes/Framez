package com.amadornes.framez.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.framez.modifier.FrameModifierRegistry;
import com.amadornes.framez.ref.References;

public class PartFactory implements IPartFactory, IPartConverter {

    public static void init() {

        PartFactory factory = new PartFactory();

        List<String> ids = new ArrayList<String>();

        for (List<IFrameModifier> mods : FrameModifierRegistry.instance().getAllCombinations(PartFrame.class))
            ids.add(FrameFactory.createFrame(PartFrame.class, mods).getType());

        MultiPartRegistry.registerParts(factory, ids.toArray(new String[ids.size()]));
        MultiPartRegistry.registerConverter(factory);
    }

    @Override
    public TMultiPart createPart(String type, boolean client) {

        if (type.equals(References.FRAME_PART_ID))
            type = References.FRAME_PART_ID + "_" + References.Modifier.MATERIAL_WOOD;

        if (type.startsWith(References.FRAME_PART_ID))
            return FrameFactory.createFrame(PartFrame.class, type);

        return null;
    }

    @Override
    public Iterable<Block> blockTypes() {

        return Arrays.asList();
    }

    @Override
    public TMultiPart convert(World world, BlockCoord location) {

        return null;
    }
}
