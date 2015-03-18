package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

public interface IMovementHandler extends IMovable {

    public boolean canHandle(World world, int x, int y, int z);

}
