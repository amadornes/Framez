package com.amadornes.trajectory.compat.fmp;

import codechicken.microblock.Microblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MicroblockMovementPlugin {

    private static int[] rotateMapFace = new int[] { 0, 1, 5, 4, 2, 3 }, rotateMapCorner = new int[] { 4, 5, 0, 1, 6, 7, 2, 3 },
            rotateMapEdge = new int[] { 2, 0, 3, 1, 8, 10, 9, 11, 5, 7, 4, 6 }, rotateMapOther = new int[] { 0, 2, 1 };

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (event.block.getTileEntity() == null || !(event.block.getTileEntity() instanceof TileMultipart))
            return;

        TileMultipart tmp = (TileMultipart) event.block.getTileEntity();
        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof Microblock && event.trajectory instanceof ITrajectoryRotation) {
                int rotAxis = ((ITrajectoryRotation) event.trajectory).getAxis() ^ 1;
                Microblock micro = (Microblock) p;
                if (p.getType().equals("mcr_face") || p.getType().equals("mcr_hllw")) {
                    micro.shape_$eq((byte) (micro.shape() & 0xF0 | MiscUtils.rotate(micro.shape() & 0x0F, rotAxis)));
                } else if (p.getType().equals("mcr_cnr")) {
                    int rY = MiscUtils.rotate((micro.shape() & 0x01) == 0 ? 0 : 1, rotAxis);
                    int rX = MiscUtils.rotate((micro.shape() & 0x02) == 0 ? 2 : 3, rotAxis);
                    int rZ = MiscUtils.rotate((micro.shape() & 0x04) == 0 ? 4 : 5, rotAxis);
                    int r = (rX == 1 || rY == 1 || rZ == 1 ? 1 : 0) | (rX == 3 || rY == 3 || rZ == 3 ? 2 : 0)
                            | (rX == 5 || rY == 5 || rZ == 5 ? 4 : 0);
                    micro.shape_$eq((byte) (micro.shape() & 0xF0 | r));
                } else if (p.getType().equals("mcr_edge")) {
                    try {
                        int s = micro.shape() & 0x0F;
                        int r = 0;

                        r |= (MiscUtils.rotate((s >> 2) << 1, rotAxis) >> 1) << 2;
                        r |= ((s & 0x04) + 1) & 0x04;

                        micro.shape_$eq((byte) (micro.shape() & 0xF0 | r));

                        System.out.println(r);

                        // r &= ~0x4;
                        // System.out.println(s + " -- " + r);
                        // r |= ((s & 4) + 1) & 4;
                        //
                        // System.out.println(s + " -> " + r);
                        //
                        // micro.shape_$eq((byte) (micro.shape() & 0xF0 | r));
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}
