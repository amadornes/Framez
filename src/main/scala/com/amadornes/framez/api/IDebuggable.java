package com.amadornes.framez.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IDebuggable {

    public boolean debug(World world, int x, int y, int z, int side, EntityPlayer player);

}
