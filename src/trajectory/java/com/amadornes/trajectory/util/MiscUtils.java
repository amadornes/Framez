package com.amadornes.trajectory.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class MiscUtils {

    public static final int[][] ROTATION_MATRIX = {//
    //
            { 0, 1, 4, 5, 3, 2, 6 },//
            { 0, 1, 5, 4, 2, 3, 6 },//
            { 5, 4, 2, 3, 0, 1, 6 },//
            { 4, 5, 2, 3, 1, 0, 6 },//
            { 2, 3, 1, 0, 4, 5, 6 },//
            { 3, 2, 0, 1, 4, 5, 6 },//
            { 0, 1, 2, 3, 4, 5, 6 },//
    };

    public static final int rotate(int dir, int axis) {

        return ROTATION_MATRIX[axis][dir];
    }

    @SuppressWarnings("rawtypes")
    public static void moveEntity(Entity entity, double dx, double dy, double dz) {

        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;

        entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
        entity.posY = entity.boundingBox.minY + entity.yOffset - entity.ySize;
        entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;

        double kx, ky, kz;

        if (entity.noClip) {
            entity.boundingBox.offset(dx, dy, dz);
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + entity.yOffset - entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;
        } else {
            entity.worldObj.theProfiler.startSection("move");
            entity.ySize *= 0.4F;

            double dx_original = dx;
            double dy_original = dy;
            double dz_original = dz;
            AxisAlignedBB bb = entity.boundingBox.copy();
            boolean flag = entity.onGround && entity.isSneaking() && entity instanceof EntityPlayer;

            // make sure player do not fall off if sneaking
            if (flag) {
                double d9;

                for (d9 = 0.05D; dx != 0.0D
                        && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(dx, -1.0D, 0.0D))
                                .isEmpty(); dx_original = dx) {
                    if (dx < d9 && dx >= -d9) {
                        dx = 0.0D;
                    } else if (dx > 0.0D) {
                        dx -= d9;
                    } else {
                        dx += d9;
                    }
                }

                for (; dz != 0.0D
                        && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, dz))
                                .isEmpty(); dz_original = dz) {
                    if (dz < d9 && dz >= -d9) {
                        dz = 0.0D;
                    } else if (dz > 0.0D) {
                        dz -= d9;
                    } else {
                        dz += d9;
                    }
                }

                while (dx != 0.0D
                        && dz != 0.0D
                        && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.getOffsetBoundingBox(dx, -1.0D, dz))
                                .isEmpty()) {
                    if (dx < d9 && dx >= -d9) {
                        dx = 0.0D;
                    } else if (dx > 0.0D) {
                        dx -= d9;
                    } else {
                        dx += d9;
                    }

                    if (dz < d9 && dz >= -d9) {
                        dz = 0.0D;
                    } else if (dz > 0.0D) {
                        dz -= d9;
                    } else {
                        dz += d9;
                    }

                    dx_original = dx;
                    dz_original = dz;
                }
            }

            List list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(dx, dy, dz));

            for (Object aList : list) {
                dy = ((AxisAlignedBB) aList).calculateYOffset(entity.boundingBox, dy);
            }

            entity.boundingBox.offset(0.0D, dy, 0.0D);

            if (!entity.field_70135_K && dy_original != dy) {
                dz = 0.0D;
                dy = 0.0D;
                dx = 0.0D;
            }

            boolean flag1 = entity.onGround || dy_original != dy && dy_original < 0.0D;
            int j;

            for (j = 0; j < list.size(); ++j) {
                dx = ((AxisAlignedBB) list.get(j)).calculateXOffset(entity.boundingBox, dx);
            }

            entity.boundingBox.offset(dx, 0.0D, 0.0D);

            if (!entity.field_70135_K && dx_original != dx) {
                dz = 0.0D;
                dy = 0.0D;
                dx = 0.0D;
            }

            for (j = 0; j < list.size(); ++j) {
                dz = ((AxisAlignedBB) list.get(j)).calculateZOffset(entity.boundingBox, dz);
            }

            entity.boundingBox.offset(0.0D, 0.0D, dz);

            if (!entity.field_70135_K && dz_original != dz) {
                dz = 0.0D;
                dy = 0.0D;
                dx = 0.0D;
            }

            int k;

            if (entity.stepHeight > 0.0F && flag1 && (flag || entity.ySize < 0.05F) && (dx_original != dx || dz_original != dz)) {
                kz = dx;
                kx = dy;
                ky = dz;
                dx = dx_original;
                dy = dy_original + entity.stepHeight;
                dz = dz_original;
                AxisAlignedBB axisalignedbb1 = entity.boundingBox.copy();
                entity.boundingBox.setBB(bb);
                list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(dx_original, dy, dz_original));

                for (k = 0; k < list.size(); ++k) {
                    dy = ((AxisAlignedBB) list.get(k)).calculateYOffset(entity.boundingBox, dy);
                }

                entity.boundingBox.offset(0.0D, dy, 0.0D);

                if (!entity.field_70135_K && dy_original != dy) {
                    dz = 0.0D;
                    dy = 0.0D;
                    dx = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    dx = ((AxisAlignedBB) list.get(k)).calculateXOffset(entity.boundingBox, dx);
                }

                entity.boundingBox.offset(dx, 0.0D, 0.0D);

                if (!entity.field_70135_K && dx_original != dx) {
                    dz = 0.0D;
                    dy = 0.0D;
                    dx = 0.0D;
                }

                for (k = 0; k < list.size(); ++k) {
                    dz = ((AxisAlignedBB) list.get(k)).calculateZOffset(entity.boundingBox, dz);
                }

                entity.boundingBox.offset(0.0D, 0.0D, dz);

                if (!entity.field_70135_K && dz_original != dz) {
                    dz = 0.0D;
                    dy = 0.0D;
                    dx = 0.0D;
                }

                if (!entity.field_70135_K && dy_original != dy) {
                    dz = 0.0D;
                    dy = 0.0D;
                    dx = 0.0D;
                } else {
                    dy = (-entity.stepHeight);

                    for (k = 0; k < list.size(); ++k) {
                        dy = ((AxisAlignedBB) list.get(k)).calculateYOffset(entity.boundingBox, dy);
                    }

                    entity.boundingBox.offset(0.0D, dy, 0.0D);
                }

                if (kz * kz + ky * ky >= dx * dx + dz * dz) {
                    dx = kz;
                    dy = kx;
                    dz = ky;
                    entity.boundingBox.setBB(axisalignedbb1);
                }
            }

            entity.worldObj.theProfiler.endSection();
            entity.worldObj.theProfiler.startSection("rest");
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + entity.yOffset - entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;

            entity.worldObj.theProfiler.endSection();
        }

        kx = entity.posX - x;
        ky = entity.posY - y;
        kz = entity.posZ - z;

        entity.prevPosX += kx;
        entity.prevPosY += ky;
        entity.prevPosZ += kz;

    }

    public static int parseCoordinate(String coord, int base) throws Exception {

        try {
            return coord.startsWith("~") ? (coord.length() == 1 ? 0 : Integer.parseInt(coord.substring(1))) + base : Integer
                    .parseInt(coord);
        } catch (Exception ex) {
            throw new RuntimeException("\"" + coord + "\" could not be parsed as a valid coordinate.");
        }
    }

}
