package com.amadornes.trajectory.compat.fmp.ae2;

import appeng.fmp.CableBusPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.compat.ae2.AE2MovementPlugin;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FMP_AE2MovementPlugin {

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (event.block.getTileEntity() == null || !(event.block.getTileEntity() instanceof TileMultipart))
            return;

        TileMultipart tmp = (TileMultipart) event.block.getTileEntity();
        for (TMultiPart p : tmp.jPartList())
            if (p instanceof CableBusPart && event.trajectory instanceof ITrajectoryRotation)
                AE2MovementPlugin.rotate(((CableBusPart) p).cb, ((ITrajectoryRotation) event.trajectory).getAxis());
    }

}
