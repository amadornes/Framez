package com.amadornes.framez.util;

import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

public class MotorPlacement {

    public static int getPlacementRotation(MovingObjectPosition mop) {

        ForgeDirection faceHit = ForgeDirection.getOrientation(mop.sideHit);

        double x = 0;
        double z = 0;

        switch (faceHit) {
        case UP:
        case DOWN:
            x = mop.hitVec.xCoord % 1;
            z = mop.hitVec.zCoord % 1;
            break;
        case EAST:
        case WEST:
            x = mop.hitVec.yCoord % 1;
            z = mop.hitVec.zCoord % 1;
            break;
        case NORTH:
        case SOUTH:
            x = mop.hitVec.xCoord % 1;
            z = mop.hitVec.yCoord % 1;
            break;
        default:
            break;
        }

        if (x < 0)
            x = 1 + x;
        if (z < 0)
            z = 1 + z;

        x -= 0.5;
        z -= 0.5;

        if (z > 0 && z > Math.abs(x)) {
            return 0;
        } else if (x > 0 && x > Math.abs(z)) {
            return 3;
        } else if (z < 0 && Math.abs(z) > Math.abs(x)) {
            return 2;
        }
        return 1;
    }

    public static ForgeDirection getPlacementDirection(MovingObjectPosition mop, ForgeDirection face) {

        int r = getPlacementRotation(mop);
        ForgeDirection d = null;

        switch (face) {
        case UP:
        case DOWN:
            d = ForgeDirection.SOUTH;
            break;
        case EAST:
        case WEST:
            r = (r + 1) % 4;
        case SOUTH:
        case NORTH:
            d = ForgeDirection.UP;
            if (r == 1 || r == 3)
                r = (r + 2) % 4;
            break;
        default:
            break;
        }
        ForgeDirection faceAbs = face;
        if (faceAbs.offsetX + faceAbs.offsetY + faceAbs.offsetZ < 0)
            faceAbs = faceAbs.getOpposite();

        for (int i = 0; i < r; i++)
            d = d.getRotation(faceAbs);

        return d;
    }
}
