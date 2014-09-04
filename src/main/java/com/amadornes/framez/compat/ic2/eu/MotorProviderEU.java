package com.amadornes.framez.compat.ic2.eu;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderEU implements IMotorProvider {

    @Override
    public String getId() {

        return "IC2";
    }

    @Override
    public String getUnlocalizedName() {

        return References.MOTOR_NAME + ".electric";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorEU.class;
    }

}
