package com.amadornes.framez.api.motor;

import net.minecraft.block.Block;

public interface IMotorInteractions extends IMotor {

    public void onTick();

    public void onNeighborBlockChange(Block block);

    public void onValidate();

    public void onInvalidate();

}
