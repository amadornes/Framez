package com.amadornes.framez.compat.ic2;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderEU implements IMotorProvider {

    public static final MotorProviderEU inst = new MotorProviderEU();

    @Override
    public String getId() {

        return Dependencies.IC2;
    }

    @Override
    public String getUnlocalizedName() {

        return References.Names.Unlocalized.MOTOR + ".electric";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorEU.class;
    }

}
