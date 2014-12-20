package com.amadornes.framez.compat.fl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameModifier;

import framesapi.IStickyBlock;

public class FunkyLocomotionFrame implements IFrame {

    private World world;
    private int x, y, z;
    private IStickyBlock block;

    public FunkyLocomotionFrame(World world, int x, int y, int z, IStickyBlock block) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    @Override
    public World world() {

        return world;
    }

    @Override
    public int x() {

        return x;
    }

    @Override
    public int y() {

        return y;
    }

    @Override
    public int z() {

        return z;
    }

    @Override
    public IFrameModifier[] getModifiers() {

        return new IFrameModifier[] {};
    }

    @Override
    public void addModifier(IFrameModifier modifier) {

    }

    @Override
    public void addModifiers(IFrameModifier... modifiers) {

    }

    @Override
    public void removeModifier(String modifier) {

    }

    @Override
    public void writeModifiersToNBT(NBTTagCompound tag) {

    }

    @Override
    public boolean hasModifier(String modifier) {

        return false;
    }

    @Override
    public void readModifiersFromNBT(NBTTagCompound tag) {

    }

    @Override
    public Object[] getConnections() {

        return new Object[] {};
    }

    @Override
    public boolean isSideBlocked(ForgeDirection side) {

        return !block.isStickySide(world(), x(), y(), z(), side);
    }

    @Override
    public boolean toggleBlock(ForgeDirection side) {

        return false;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 6;
    }

}
