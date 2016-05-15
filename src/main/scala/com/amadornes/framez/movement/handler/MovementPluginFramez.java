package com.amadornes.framez.movement.handler;

import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.modifier.FrameFactory;
import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MovementPluginFramez {

    @SubscribeEvent
    public void onMovementFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (!(event.trajectory instanceof ITrajectoryRotation))
            return;
        int rotAxis = ((ITrajectoryRotation) event.trajectory).getAxis() ^ 1;

        if (event.block.getBlock() instanceof BlockFrame) {
            int mask = ((BlockFrame) event.block.getBlock()).id * 16 + event.block.getMetadata();
            int newMask = 0;
            for (int i = 0; i < 6; i++)
                if ((mask & (1 << i)) != 0)
                    newMask |= 1 << MiscUtils.rotate(i, rotAxis);
            event.block.setBlock(FramezBlocks.frames.get(FrameFactory.getIdentifier("frame"
                    + (newMask < 16 ? "0" : (newMask < 32 ? "1" : (newMask < 48 ? "2" : "3"))),
                    ((BlockFrame) event.block.getBlock()).material)));
            event.block.setMetadata(newMask % 16);
        }
    }

}
