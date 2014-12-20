package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.framez.tile.TileMotor;

public class MotorCache {

    private static List<TileMotor> motors = new ArrayList<TileMotor>();

    public static void loadMotor(TileMotor motor) {

        if (!motors.contains(motor))
            motors.add(motor);
    }

    public static void unloadMotor(TileMotor motor) {

        motors.remove(motor);
    }

    public static List<TileMotor> getLoadedMotors() {

        return motors;
    }

}
