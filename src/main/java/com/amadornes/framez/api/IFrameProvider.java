package com.amadornes.framez.api;

import net.minecraft.world.World;

public interface IFrameProvider {

    public IFrame getFrameAt(World world, int x, int y, int z);

}
