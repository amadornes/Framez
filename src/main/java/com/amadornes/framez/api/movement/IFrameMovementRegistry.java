package com.amadornes.framez.api.movement;

import java.util.Collection;

import net.minecraft.world.World;

public interface IFrameMovementRegistry {

    public void registerMovementDataProvider(IMovementDataProvider provider);

    public void registerMovementHandler(IMovementHandler handler);

    public void registerStickyProvider(IStickyProvider provider);

    public void registerStickinessHandler(IStickinessHandler handler);

    public Collection<IMovable> findMovables(World world, int x, int y, int z);

    public Collection<ISticky> findStickies(World world, int x, int y, int z);

    public Collection<IFrame> findFrames(World world, int x, int y, int z);

}
