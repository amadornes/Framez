package com.amadornes.framez.compat.rf;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderRF implements IMotorProvider {

    @Override
    public String getId() {

        return "rf";
    }

    @Override
    public String getUnlocalizedName() {

        return References.Names.Unlocalized.MOTOR + ".rf";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorRF.class;
    }

}
