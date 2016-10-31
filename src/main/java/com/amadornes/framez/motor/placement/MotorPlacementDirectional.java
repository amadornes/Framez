package com.amadornes.framez.motor.placement;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.util.FramezUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.RayTraceResult;

public class MotorPlacementDirectional implements IMotorPlacement<Pair<EnumFacing, EnumFacing>> {

    @Override
    public Pair<EnumFacing, EnumFacing> getPlacementData(EntityPlayer player, RayTraceResult hit) {

        return Pair.of(hit.sideHit.getOpposite(), getPlacementDirection(hit.sideHit, (float) (hit.hitVec.xCoord % 1),
                (float) (hit.hitVec.yCoord % 1), (float) (hit.hitVec.zCoord % 1)));
    }

    @Override
    public void renderPlacementArrow(Pair<EnumFacing, EnumFacing> data) {

    }

    public static int getPlacementRotation(EnumFacing faceHit, float hitX, float hitY, float hitZ) {

        float x = 0;
        float z = 0;

        switch (faceHit) {
        case DOWN:
        case UP:
            x = hitX;
            z = hitZ;
            break;
        case WEST:
        case EAST:
            x = hitY;
            z = hitZ;
            break;
        case NORTH:
        case SOUTH:
            x = hitX;
            z = hitY;
            break;
        default:
            break;
        }

        if (x < 0) {
            x = 1 + x;
        }
        if (z < 0) {
            z = 1 + z;
        }

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

    public static EnumFacing getPlacementDirection(EnumFacing faceHit, float hitX, float hitY, float hitZ) {

        int r = getPlacementRotation(faceHit, hitX, hitY, hitZ);
        EnumFacing d = null;

        switch (faceHit) {
        case DOWN:
        case UP:
            d = EnumFacing.SOUTH;
            break;
        case WEST:
        case EAST:
            r = (r + 1) % 4;
        case NORTH:
        case SOUTH:
            d = EnumFacing.UP;
            if (r == 1 || r == 3) {
                r = (r + 2) % 4;
            }
            break;
        default:
            break;
        }
        EnumFacing faceAbs = faceHit;
        if (faceAbs.getAxisDirection() == AxisDirection.NEGATIVE) {
            faceAbs = faceAbs.getOpposite();
        }

        for (int i = 0; i < r; i++) {
            d = FramezUtils.rotate(d, faceAbs);
        }

        return d;
    }

}
