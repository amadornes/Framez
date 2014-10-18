package com.amadornes.framez.compat.bm;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderBM implements IMotorProvider {

    public static final MotorProviderBM inst = new MotorProviderBM();

    @Override
    public String getId() {

        return Dependencies.BM;
    }

    @Override
    public String getUnlocalizedName() {

        return References.Names.Unlocalized.MOTOR + ".blood";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorBM.class;
    }

}
