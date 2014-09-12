package com.amadornes.framez.api;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IMotorRegistry {

    public void registerMotor(IMotorProvider provider);

    public IMotorProvider[] getRegisteredMotors();

    @SideOnly(Side.CLIENT)
    public void registerSpecialRenderer(IRenderMotorSpecial renderer);

    @SideOnly(Side.CLIENT)
    public IRenderMotorSpecial[] getRenderers(TileMotor tile, ForgeDirection face);

}
