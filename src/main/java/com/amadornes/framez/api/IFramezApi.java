package com.amadornes.framez.api;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IMovementApi;

public interface IFramezApi {

    public IModifierRegistry getModifierRegistry();

    public IMotorRegistry getMotorRegistry();

    public IMovementApi getMovementApi();

    public IFrame getFrame(World world, int x, int y, int z);

}
