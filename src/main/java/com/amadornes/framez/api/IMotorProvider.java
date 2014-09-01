package com.amadornes.framez.api;

import com.amadornes.framez.tile.TileMotor;

public interface IMotorProvider {

    public String getId();

    public String getUnlocalizedName();

    public Class<? extends TileMotor> getTileClass();

}
