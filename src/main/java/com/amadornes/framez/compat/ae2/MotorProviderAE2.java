package com.amadornes.framez.compat.ae2;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderAE2 implements IMotorProvider {

    public static final MotorProviderAE2 inst = new MotorProviderAE2();

    @Override
    public String getId() {

        return Dependencies.AE2;
    }

    @Override
    public String getUnlocalizedName() {

        return References.Names.Unlocalized.MOTOR + ".applied";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorAE2.class;
    }

}
