package com.amadornes.framez.api.compat;

import net.minecraft.world.World;

public interface IFMPCompatRegistry {

    public void registerIgnoredCover(String material);

    public boolean hasCover(World world, int x, int y, int z, int side);

    public boolean hasCover(World world, int x, int y, int z, int side, String material);

}
