package com.amadornes.framez.util;

import com.amadornes.trajectory.util.MiscUtils;

public class MotorPlacement {

    public static int getPlacementRotation(int faceHit, float hitX, float hitY, float hitZ) {

        float x = 0;
        float z = 0;

        switch (faceHit) {
        case 0:
        case 1:
            x = hitX;
            z = hitZ;
            break;
        case 4:
        case 5:
            x = hitY;
            z = hitZ;
            break;
        case 2:
        case 3:
            x = hitX;
            z = hitY;
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

    public static int getPlacementDirection(int faceHit, float hitX, float hitY, float hitZ) {

        int r = getPlacementRotation(faceHit, hitX, hitY, hitZ);
        int d = -1;

        switch (faceHit) {
        case 0:
        case 1:
            d = 3;
            break;
        case 4:
        case 5:
            r = (r + 1) % 4;
        case 2:
        case 3:
            d = 1;
            if (r == 1 || r == 3)
                r = (r + 2) % 4;
            break;
        default:
            break;
        }
        int faceAbs = faceHit;
        if (FramezUtils.getOffsetX(faceAbs) + FramezUtils.getOffsetY(faceAbs) + FramezUtils.getOffsetZ(faceAbs) < 0)
            faceAbs = faceAbs ^ 1;

        for (int i = 0; i < r; i++)
            d = MiscUtils.rotate(d, faceAbs);

        return d;
    }
}