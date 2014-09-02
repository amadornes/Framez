package com.amadornes.framez.api.movement;

import net.minecraftforge.common.util.ForgeDirection;

public interface IMovementListener {

    public void onStartMoving(Object o, ForgeDirection direction);

    public void onFinishMoving(Object o, ForgeDirection direction);

    public boolean canHandle(Object o);

}
