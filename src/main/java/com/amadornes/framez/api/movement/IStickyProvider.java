package com.amadornes.framez.api.movement;

import net.minecraft.world.World;

public interface IStickyProvider {

    public ISticky getStickyAt(World world, int x, int y, int z);

    public IFrame getFrameAt(World world, int x, int y, int z);

}
