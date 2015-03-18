package com.amadornes.framez.api.movement;

import java.util.Collection;

import net.minecraft.world.World;

public interface IMovingStructure {

    public World getWorld();

    public Collection<IMovingBlock> getBlocks();

    public IMovement getMovement();

    public double getProgress();

    public double getInterpolatedProgress(double frame);

}
