package com.amadornes.framez.compat.vanilla;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderDC implements IMotorProvider {

    @Override
    public String getId() {

        return "Vanilla";
    }

    @Override
    public String getUnlocalizedName() {

        return References.MOTOR_NAME + ".dc";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorDC.class;
    }

}
