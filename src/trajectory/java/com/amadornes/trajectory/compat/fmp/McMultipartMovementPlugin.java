package com.amadornes.trajectory.compat.fmp;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.ButtonPart;
import codechicken.multipart.minecraft.LeverPart;
import codechicken.multipart.minecraft.McSidedMetaPart;
import codechicken.multipart.minecraft.RedstoneTorchPart;
import codechicken.multipart.minecraft.TorchPart;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class McMultipartMovementPlugin {

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        if (event.block.getTileEntity() == null || !(event.block.getTileEntity() instanceof TileMultipart))
            return;

        TileMultipart tmp = (TileMultipart) event.block.getTileEntity();
        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof McSidedMetaPart && event.trajectory instanceof ITrajectoryRotation) {
                int rotAxis = ((ITrajectoryRotation) event.trajectory).getAxis() ^ 1;
                if (p instanceof LeverPart) {
                    LeverPart part = (LeverPart) p;
                    part.meta = (byte) (LeverPart.sideMetaMap[MiscUtils.rotate(LeverPart.metaSideMap[part.meta & 7], rotAxis)] ^ (part
                            .active() ? 8 : 0));
                } else if (p instanceof RedstoneTorchPart) {
                    RedstoneTorchPart part = (RedstoneTorchPart) p;
                    part.meta = (byte) (RedstoneTorchPart.sideMetaMap[MiscUtils.rotate(TorchPart.metaSideMap[part.meta], rotAxis)] ^ (part
                            .active() ? 0x10 : 0));
                } else if (p instanceof TorchPart) {
                    TorchPart part = (TorchPart) p;
                    part.meta = (byte) TorchPart.sideMetaMap[MiscUtils.rotate(TorchPart.metaSideMap[part.meta], rotAxis)];
                } else if (p instanceof ButtonPart) {
                    ButtonPart part = (ButtonPart) p;
                    part.meta = (byte) ((ButtonPart.sideMetaMap[MiscUtils.rotate(ButtonPart.metaSideMap[part.meta & 7], rotAxis)] ^ (part
                            .pressed() ? 8 : 0)) ^ (part.sensitive() ? 0x10 : 0));
                }
            }
        }
    }
}
