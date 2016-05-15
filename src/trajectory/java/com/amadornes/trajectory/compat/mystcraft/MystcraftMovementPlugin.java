package com.amadornes.trajectory.compat.mystcraft;

import net.minecraft.tileentity.TileEntity;

import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.TrajectoryEvent.TrajectoryEventMove;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MystcraftMovementPlugin {

    @SubscribeEvent
    public void onMoveFinishPre(TrajectoryEventMove.Block.Finish.Pre event) {

        TileEntity te = event.block.getTileEntity();
        if (te == null || !(event.trajectory instanceof ITrajectoryRotation))
            return;
        ITrajectoryRotation rot = (ITrajectoryRotation) event.trajectory;
        int axis = rot.getAxis();

        if (te instanceof TileEntityBookRotateable && (axis == 0 || axis == 1)) {
            TileEntityBookRotateable tile = (TileEntityBookRotateable) te;
            int yaw = (tile.getYaw() + 270 * rot.getRotations()) % 360;
            tile.setYaw(yaw);

            event.shouldApplyTransformation = false;
        }
    }
}
