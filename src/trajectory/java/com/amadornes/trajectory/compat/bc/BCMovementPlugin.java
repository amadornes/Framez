package com.amadornes.trajectory.compat.bc;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.transport.pluggable.PipePluggable;
import buildcraft.silicon.TileLaser;
import buildcraft.transport.TileGenericPipe;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BCMovementPlugin {

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        TileEntity te = event.block.getTileEntity();
        if (te == null || !(event.trajectory instanceof ITrajectoryRotation))
            return;
        ITrajectoryRotation rot = (ITrajectoryRotation) event.trajectory;
        int axis = rot.getAxis();

        if (te instanceof TileGenericPipe) {
            TileGenericPipe tgp = (TileGenericPipe) te;
            boolean[] rotatedConnections = new boolean[6];
            int[] rotatedRedstoneInput = new int[6];
            PipePluggable[] rotatedPluggables = new PipePluggable[6];
            for (int i = 0; i < 6; i++) {
                int j = i;
                for (int k = 0; k < rot.getRotations(); k++)
                    j = MiscUtils.rotate(j, axis);
                rotatedConnections[i] = tgp.pipeConnectionsBuffer[j];
                rotatedRedstoneInput[i] = tgp.redstoneInputSide[j];
                PipePluggable p = rotatedPluggables[i] = tgp.pluggableState.getPluggables()[j];
                if (p != null)
                    tgp.pipe.eventBus.unregisterHandler(p);
            }
            for (int i = 0; i < 6; i++) {
                tgp.pipeConnectionsBuffer[i] = rotatedConnections[i];
                tgp.redstoneInputSide[i] = rotatedRedstoneInput[i];
                tgp.pluggableState.getPluggables()[i] = rotatedPluggables[i];
                if (rotatedPluggables[i] != null) {
                    tgp.pipe.eventBus.registerHandler(rotatedPluggables[i]);
                    rotatedPluggables[i].onAttachedPipe(tgp, ForgeDirection.getOrientation(i));
                }
            }
            tgp.pluggableState.setPluggables(rotatedPluggables);
            tgp.scheduleNeighborChange();
        } else if (te instanceof TileLaser) {
            for (int k = 0; k < rot.getRotations(); k++)
                event.block.setMetadata(MiscUtils.rotate(event.block.getMetadata(), axis ^ 1));
        }
    }
}
