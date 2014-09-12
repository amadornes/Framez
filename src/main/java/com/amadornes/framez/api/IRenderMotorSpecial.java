package com.amadornes.framez.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderMotorSpecial {

    public boolean shouldRender(TileMotor motor, ForgeDirection face);

    public boolean shouldRender(ItemStack item, ForgeDirection face);

    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame);

    public void renderSpecial(ItemStack item, ForgeDirection face, float frame);

}
