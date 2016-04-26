package com.amadornes.framez.api;

import com.amadornes.framez.api.frame.IFrameRegistry;
import com.amadornes.framez.api.motor.IMotorRegistry;
import com.amadornes.framez.api.movement.IMovementRegistry;

public final class FramezAPI {

    public static interface IFramezAPI {

        public IMotorRegistry getMotorRegistry();

        public IFrameRegistry getFrameRegistry();

        public IMovementRegistry getMovementRegistry();

    }

    public static final IFramezAPI INSTANCE;

    static {
        try {
            INSTANCE = (IFramezAPI) Class.forName(FramezAPI.class.getPackage().getName().replace("api", "FramezAPIImpl")).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
