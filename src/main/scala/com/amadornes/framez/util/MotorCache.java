package com.amadornes.framez.util;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.framez.tile.TileMotor;

public class MotorCache {

    private static List<TileMotor> loadedMotors = new ArrayList<TileMotor>();

    public static void onLoad(TileMotor motor) {

        loadedMotors.add(motor);
    }

    public static void onUnload(TileMotor motor) {

        loadedMotors.remove(motor);
    }

    public static List<TileMotor> getLoadedMotors() {

        return loadedMotors;
    }

}
