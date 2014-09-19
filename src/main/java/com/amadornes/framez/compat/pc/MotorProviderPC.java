package com.amadornes.framez.compat.pc;

import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.ref.Dependencies;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

public class MotorProviderPC implements IMotorProvider {

    public static final MotorProviderPC inst = new MotorProviderPC();

    @Override
    public String getId() {

        return Dependencies.PC;
    }

    @Override
    public String getUnlocalizedName() {

        return References.Names.Unlocalized.MOTOR + ".pneumatic";
    }

    @Override
    public Class<? extends TileMotor> getTileClass() {

        return TileMotorPC.class;
    }

}
