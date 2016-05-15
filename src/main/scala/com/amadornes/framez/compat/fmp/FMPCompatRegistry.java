package com.amadornes.framez.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.Microblock;
import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.compat.IFMPCompatRegistry;

public class FMPCompatRegistry implements IFMPCompatRegistry {

    public static List<String> materials = new ArrayList<String>();

    @Override
    public void registerIgnoredCover(String material) {

        materials.add(material);
    }

    @Override
    public boolean hasCover(World world, int x, int y, int z, int side) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return false;
        TMultiPart p = tmp.partMap(PartMap.face(side).mask);
        if (p == null)
            return false;
        return p instanceof FaceMicroblock;
    }

    @Override
    public boolean hasCover(World world, int x, int y, int z, int side, String material) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return false;
        TMultiPart p = tmp.partMap(PartMap.face(side).mask);
        if (p == null)
            return false;
        return p instanceof FaceMicroblock && MicroMaterialRegistry.materialName(((Microblock) p).getMaterial()).equals(material);
    }

}
