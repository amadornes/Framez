package com.amadornes.framez.api.movement;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMovementListener {

    public void onStartMoving(ForgeDirection direction);

    public void onFinishMoving(ForgeDirection direction);

}
