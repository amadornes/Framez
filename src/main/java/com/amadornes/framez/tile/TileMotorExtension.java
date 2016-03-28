package com.amadornes.framez.tile;

import com.amadornes.framez.api.DynamicReference;

import net.minecraft.tileentity.TileEntity;

public class TileMotorExtension extends TileEntity {

    private DynamicReference<TileMotor> motor;
    private int dist;

    public TileMotorExtension() {

    }

}
