package com.amadornes.framez.api.movement;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IMovingBlock {

    public World getWorld();

    public World getWorldWrapper();

    public int getX();

    public int getY();

    public int getZ();

    public Block getBlock();

    public int getMetadata();

    public TileEntity getTileEntity();

    public double getMoved();

    public double getSpeed();

    public ForgeDirection getDirection();

    public void setBlock(Block block);

    public void setMetadata(int metadata);

    public void setTileEntity(TileEntity tileentity);

    public void remove_do(boolean invalidate, boolean validate);

    public void place_do(boolean invalidate, boolean validate);

}
