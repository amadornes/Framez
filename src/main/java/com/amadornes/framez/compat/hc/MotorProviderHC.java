package com.amadornes.framez.compat.hc;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderHC implements IMotorProvider {

    @Override
    public String getId() {

        return "HydCraft";
    }

    @Override
    public String getUnlocalizedName() {

        return "Hydraulic Motor";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorHC.class;
    }

}
