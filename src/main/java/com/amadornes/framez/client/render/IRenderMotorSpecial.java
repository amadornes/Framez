package com.amadornes.framez.client.render;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderMotorSpecial {

    public boolean shouldRender(TileMotor motor, ForgeDirection face);

    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame);

}
