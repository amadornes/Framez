package com.amadornes.framez.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.FaceMicroblock;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.part.PartFrame;

public class Utils {

    public static PartFrame getFrame(World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMultipart) {
            return getFrame((TileMultipart) te);
        }
        return null;
    }

    public static PartFrame getFrame(TileMultipart te) {

        for (TMultiPart p : te.jPartList()) {
            if (p instanceof PartFrame) {
                return (PartFrame) p;
            }
        }
        return null;
    }

    public static boolean occlusionTest(World world, int x, int y, int z, ForgeDirection face) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMultipart) {
            return occlusionTest((TileMultipart) te, face);
        }
        return false;
    }

    public static boolean occlusionTest(TileMultipart tile, ForgeDirection face) {

        if (tile != null) {
            TileMultipart te = tile;
            NormallyOccludedPart box = new NormallyOccludedPart(
                    new Cuboid6(3 / 16D, 0, 3 / 16D, 12 / 16D, 1 / 16D, 12 / 16D).apply(Rotation.sideRotations[face.ordinal()].at(Vector3.center)));
            for (TMultiPart p : te.jPartList()) {
                if (p instanceof PartFrame)
                    continue;
                if (p instanceof FaceMicroblock)
                    if (((FaceMicroblock) p).getSlot() == face.ordinal())
                        return false;
                if (!p.occlusionTest(box))
                    return false;
            }

            return true;
        }

        return false;
    }

}
