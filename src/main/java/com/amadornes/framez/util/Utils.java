package com.amadornes.framez.util;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
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

    public static void addConnected(List<BlockCoord> blocks, PartFrame frame) {

        int i = 0;
        for (Object o : frame.getConnections()) {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            if (o != null) {
                if (o instanceof PartFrame) {
                    PartFrame f = (PartFrame) o;
                    BlockCoord c = new BlockCoord(f.x(), f.y(), f.z());
                    if (!blocks.contains(c)) {
                        blocks.add(c);
                        addConnected(blocks, f);
                    }
                } else if (o instanceof Boolean) {
                    BlockCoord c = new BlockCoord(frame.x() + d.offsetX, frame.y() + d.offsetY, frame.z() + d.offsetZ);
                    if (!blocks.contains(c))
                        blocks.add(c);
                } else if (o instanceof Integer) {
                    if (((Integer) o).intValue() > 1) {
                        if (getFrame(frame.world(), frame.x() + d.offsetX, frame.y() + d.offsetY, frame.z() + d.offsetZ) == null) {
                            BlockCoord c = new BlockCoord(frame.x() + d.offsetX, frame.y() + d.offsetY, frame.z() + d.offsetZ);
                            if (!blocks.contains(c))
                                blocks.add(c);
                        }
                    }
                }
            }
            i++;
        }
    }

}
