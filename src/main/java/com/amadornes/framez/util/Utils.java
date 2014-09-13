package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.movement.HandlingPriority;
import com.amadornes.framez.api.movement.HandlingPriority.Priority;
import com.amadornes.framez.part.PartFrame;

public class Utils {

    public static IFrame getFrame(World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMultipart) {
            return getFrame((TileMultipart) te);
        }
        return null;
    }

    public static IFrame getFrame(TileMultipart te) {

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
                    new Cuboid6(3 / 16D, 0, 3 / 16D, 12 / 16D, 3 / 16D, 12 / 16D).apply(Rotation.sideRotations[face.ordinal()].at(Vector3.center)));
            for (TMultiPart p : te.jPartList()) {
                if (p instanceof PartFrame)
                    continue;
                if (!p.occlusionTest(box))
                    return true;
            }

            return false;
        }

        return false;
    }

    public static int getMicroblockSize(TileMultipart tile, ForgeDirection face) {

        if (tile != null) {
            TileMultipart te = tile;
            for (TMultiPart p : te.jPartList()) {
                if (p instanceof PartFrame)
                    continue;
                if (p instanceof FaceMicroblock)
                    if (((FaceMicroblock) p).getSlot() == face.ordinal())
                        return ((FaceMicroblock) p).getSize();
            }

            return 0;
        }

        return 0;
    }

    public static TileMultipart getMultipartTile(World w, int x, int y, int z) {

        TileEntity te = w.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMultipart)
            return (TileMultipart) te;
        return null;
    }

    public static <T> List<T> sortByPriority(Map<T, HandlingPriority.Priority> map) {

        List<T> sorted = new ArrayList<T>();

        for (T h : map.keySet())
            if (map.get(h) == Priority.HIGH)
                sorted.add(h);

        for (T h : map.keySet())
            if (map.get(h) == Priority.MEDIUM)
                sorted.add(h);

        for (T h : map.keySet())
            if (map.get(h) == Priority.LOW)
                sorted.add(h);

        return sorted;
    }

}
