package com.amadornes.framez.api.movement;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IMovable {

    public BlockMovementType getMovementType(World world, int x, int y, int z, ForgeDirection side, IMovement movement);

    public boolean startMoving(IMovingBlock block);

    public boolean finishMoving(IMovingBlock block);

}
