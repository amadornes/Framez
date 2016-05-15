package com.amadornes.framez.api.movement;

import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.ITrajectory;

public interface IMovement extends ITrajectory {

    public IMovement withSpeed(double speed);

    public boolean rotate(int axis, IMotor motor);

    public double getSpeedMultiplier(BlockSet blocks);

    public static interface IMovementSlide extends IMovement, ITrajectoryTranslation {

        public int getDirection();

        public void setDirection(int dir);
    }

    public static interface IMovementRotation extends IMovement, ITrajectoryRotation {

        public void setAxis(int axis);

    }

    public static interface IMovementBlink extends IMovement {

        public int getDirection();

        public void setDirection(int dir);

        public int getDistance();

        public void setDistance(int distance);
    }

}
