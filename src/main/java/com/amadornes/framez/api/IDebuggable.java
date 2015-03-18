package com.amadornes.framez.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IDebuggable {

    public boolean debug(World world, int x, int y, int z, ForgeDirection face, EntityPlayer player);

}
