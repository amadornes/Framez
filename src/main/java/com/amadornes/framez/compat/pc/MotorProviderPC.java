package com.amadornes.framez.compat.pc;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderPC implements IMotorProvider {

    @Override
    public String getId() {

        return "PneumaticCraft";
    }

    @Override
    public String getUnlocalizedName() {

        return "Pneumatic Motor";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorPC.class;
    }

}
