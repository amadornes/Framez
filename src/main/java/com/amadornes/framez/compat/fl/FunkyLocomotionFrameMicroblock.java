package com.amadornes.framez.compat.fl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TileMultipart;

import com.amadornes.framez.api.IFrame;
import com.amadornes.framez.api.IFrameIgnoreMicroblocks;
import com.amadornes.framez.api.IFrameModifier;

public class FunkyLocomotionFrameMicroblock implements IFrame, IFrameIgnoreMicroblocks {

    private TileMultipart tmp;

    public FunkyLocomotionFrameMicroblock(TileMultipart tmp) {

        this.tmp = tmp;
    }

    @Override
    public World world() {

        return tmp.getWorldObj();
    }

    @Override
    public int x() {

        return tmp.xCoord;
    }

    @Override
    public int y() {

        return tmp.yCoord;
    }

    @Override
    public int z() {

        return tmp.zCoord;
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

        if (FrameProviderFL.isFLMicroblock(tmp, side.ordinal()))
            return false;

        return true;
    }

    @Override
    public boolean toggleBlock(ForgeDirection side) {

        return false;
    }

    @Override
    public int getMaxCarriedBlocks() {

        return 6;
    }

    @Override
    public boolean shouldIgnoreMicroblocks(ForgeDirection side) {

        return true;// !isSideBlocked(side);
    }

}
