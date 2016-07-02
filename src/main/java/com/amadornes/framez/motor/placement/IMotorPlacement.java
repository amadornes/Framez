package com.amadornes.framez.motor.placement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMotorPlacement<D> {

    public D getPlacementData(EntityPlayer player, RayTraceResult hit);

    @SideOnly(Side.CLIENT)
    public void renderPlacementArrow(D data);

}
