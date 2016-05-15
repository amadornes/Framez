package com.amadornes.framez.movement.handler;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.block.BlockStopper;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.trajectory.api.IBlockMovementHandler;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.api.vec.BlockPos;

public class BlockMovementHandlerStopper implements IBlockMovementHandler {

    @Override
    public boolean canHandle(IMovingBlock block, ITrajectory trajectory) {

        if (block.getStructure() == null)
            return false;
        if (!(block.getStructure().getCallback() != null && block.getStructure().getCallback() instanceof IMotor))
            return false;
        IMotor motor = (IMotor) block.getStructure().getCallback();
        if (block.getBlock() instanceof BlockStopper && block.getMetadata() != 1) {
            BlockPos blockPos = trajectory.transformPos(block.getPosition());
            return blockPos.equals(new BlockPos(motor.getX(), motor.getY(), motor.getZ()).offset(motor.getFace()));
        }
        return false;
    }

    @Override
    public boolean canBeMoved(IMovingBlock block, ITrajectory trajectory) {

        return true;
    }

    @Override
    public void startMoving(IMovingBlock block, ITrajectory trajectory) {

        TrajectoryAPI.instance().defaultStartMoving(block, false, false, true);

        TileMotor te = ((TileMotor) block.getStructure().getCallback());
        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData d = te.getUpgrades()[i];
            if (d != null && d.getUpgrade().getType().equals("bouncy"))
                return;
        }
        te.setNeedsRestart();
    }

    @Override
    public void finishMoving(IMovingBlock block, ITrajectory trajectory) {

        TrajectoryAPI.instance().defaultFinishMoving(block, false, false);
    }

}
