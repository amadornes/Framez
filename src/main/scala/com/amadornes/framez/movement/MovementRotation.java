package com.amadornes.framez.movement;

import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementRotation;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.movement.TrajectoryRotation;
import com.amadornes.trajectory.util.MiscUtils;

public class MovementRotation extends TrajectoryRotation implements IMovementRotation {

    public MovementRotation(BlockPos origin, int axis, double speed) {

        super(origin, axis, 1, speed);
    }

    public MovementRotation() {

        super();
    }

    @Override
    public void setAxis(int axis) {

        this.axis = axis;
    }

    @Override
    public MovementRotation copy() {

        return new MovementRotation(getOriginBlock(), getAxis(), getSpeed());
    }

    @Override
    public IMovement withSpeed(double speed) {

        return new MovementRotation(getOriginBlock(), getAxis(), speed);
    }

    @Override
    public boolean rotate(int axis, IMotor motor) {

        if (axis == getAxis() || axis == (getAxis() ^ 1)) {
            setAxis(getAxis() ^ 1);
            motor.sendUpdate();
        } else {
            motor.setFace(MiscUtils.rotate(motor.getFace(), axis));
        }

        return true;
    }

    @Override
    public double getSpeedMultiplier(BlockSet blocks) {

        double l = 0;
        Vector3 o = getOrigin();

        for (IMovingBlock b : blocks)
            l = Math.max(l, o.copy().sub(new Vector3(b.getPosition())).mag());

        if (l > 0)
            return 1 / Math.pow(l, 1 / 2D);
        return 1;
    }

}
