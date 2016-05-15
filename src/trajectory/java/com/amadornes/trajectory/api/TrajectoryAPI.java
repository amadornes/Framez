package com.amadornes.trajectory.api;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.ModContainer;

/**
 * Class used to get an instance of {@link IMovementManager}.
 */
public class TrajectoryAPI {

    private static IMovementManager instance;

    public static IMovementManager instance() {

        return instance;
    }

    public static void setup(IMovementManager movementManager) {

        ModContainer mod = Loader.instance().activeModContainer();
        if (!mod.getModId().equals("Trajectory"))
            throw new LoaderException("The mod \"" + mod.getName() + "\" (" + mod.getModId()
                    + ") is attempting to initialize Trajectory's API and it's not supposed to. Report this to the author!");

        TrajectoryAPI.instance = movementManager;
    }

}
