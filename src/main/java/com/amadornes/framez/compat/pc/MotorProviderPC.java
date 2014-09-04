package com.amadornes.framez.compat.pc;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderPC implements IMotorProvider {

    @Override
    public String getId() {

        return "PneumaticCraft";
    }

    @Override
    public String getUnlocalizedName() {

        return References.MOTOR_NAME + ".pneumatic";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorPC.class;
    }

}
