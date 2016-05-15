package com.amadornes.trajectory.compat.te;

import net.minecraft.tileentity.TileEntity;
import cofh.api.tileentity.IReconfigurableFacing;
import cofh.api.tileentity.IReconfigurableSides;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.amadornes.trajectory.util.MiscUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TEMovementPlugin {

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        TileEntity te = event.block.getTileEntity();
        if (te == null || !(event.trajectory instanceof ITrajectoryRotation))
            return;
        ITrajectoryRotation rot = (ITrajectoryRotation) event.trajectory;
        int axis = rot.getAxis();

        if (axis != 0 && axis != 1)
            return;

        if (te instanceof IReconfigurableFacing && te instanceof IReconfigurableSides) {
            IReconfigurableFacing irf = (IReconfigurableFacing) te;
            IReconfigurableSides irs = (IReconfigurableSides) te;

            int[] sideCache = new int[6];
            for (int i = 0; i < 6; i++) {
                int j = i;
                for (int k = 0; k < rot.getRotations(); k++)
                    j = MiscUtils.rotate(j, rot.getAxis());
                sideCache[i] = irs.getNumConfig(j);
            }

            int facing = irf.getFacing();
            for (int i = 0; i < rot.getRotations(); i++)
                facing = MiscUtils.rotate(facing, axis ^ 1);
            irf.setFacing(facing);

            for (int i = 0; i < 6; i++)
                irs.setSide(i, sideCache[i]);

            event.shouldApplyTransformation = false;
        } else if (te instanceof IReconfigurableFacing) {
            IReconfigurableFacing irf = (IReconfigurableFacing) te;

            int facing = irf.getFacing();
            for (int i = 0; i < rot.getRotations(); i++)
                facing = MiscUtils.rotate(facing, axis ^ 1);
            irf.setFacing(facing);

            event.shouldApplyTransformation = false;
        } else if (te instanceof IReconfigurableSides) {
            IReconfigurableSides irs = (IReconfigurableSides) te;

            int[] sideCache = new int[6];
            for (int i = 0; i < 6; i++) {
                int j = i;
                for (int k = 0; k < rot.getRotations(); k++)
                    j = MiscUtils.rotate(j, rot.getAxis());
                sideCache[i] = irs.getNumConfig(j);
            }

            for (int i = 0; i < 6; i++)
                irs.setSide(i, sideCache[i]);

            event.shouldApplyTransformation = false;
        }
    }
}
