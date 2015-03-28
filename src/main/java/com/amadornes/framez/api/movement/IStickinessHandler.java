package com.amadornes.framez.api.movement;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IStickinessHandler extends ISticky {

    public boolean canHandle(World world, int x, int y, int z, ForgeDirection side);

}
