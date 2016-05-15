package com.amadornes.trajectory.api;

import net.minecraft.world.World;

public interface IDynamicWorldReference {

    public World getWorld(IMovingBlock block);

}
