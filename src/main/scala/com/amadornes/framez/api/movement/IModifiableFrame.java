package com.amadornes.framez.api.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.trajectory.api.vec.Vector3;

public interface IModifiableFrame extends IFrame {

    public IFrameSideModifier[] getSideModifiers(int side);

    public void addSideModifier(int side, IFrameSideModifier modifier);

    public void removeSideModifier(int side, IFrameSideModifier modifier);

    public void update();

    public void onNeighborChanged();

    public boolean onActivated(EntityPlayer player, int side, Vector3 hit);

    public void notifyChange();

    public void sendUpdate();

    public void writeFrame(NBTTagCompound tag);

    public void readFrame(NBTTagCompound tag);
}