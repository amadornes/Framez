package com.amadornes.framez.compat.fmp;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MovementPluginFMPFrame {

    @SubscribeEvent
    public void onMovementFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (!(event.trajectory instanceof ITrajectoryRotation) || !(event.block.getTileEntity() instanceof TileMultipart))
            return;
        PartFrame frame = null;
        for (TMultiPart p : ((TileMultipart) event.block.getTileEntity()).jPartList())
            if (p instanceof PartFrame)
                frame = (PartFrame) p;
        if (frame == null)
            return;

        int rotAxis = ((ITrajectoryRotation) event.trajectory).getAxis() ^ 1;
        boolean[] hidden = new boolean[6], blocked = new boolean[6];
        for (int i = 0; i < 6; i++) {
            hidden[MiscUtils.rotate(i, rotAxis)] = frame.hidden[i];
            blocked[MiscUtils.rotate(i, rotAxis)] = frame.blocked[i];
        }
        for (int i = 0; i < 6; i++) {
            frame.hidden[i] = hidden[i];
            frame.blocked[i] = blocked[i];
        }
    }
}
