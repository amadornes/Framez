package com.amadornes.framez.api.movement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.vec.Vector3;

public interface IMotor {

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public IMotorModifier[] getModifiers();

    public IMotorUpgradeData[] getUpgrades();

    public int getFace();

    public void setFace(int face);

    public boolean rotate(int axis);

    public IMovement getMovement();

    public IMovement getMovement(BlockSet blocks);

    public double injectPower(double amount, boolean simulated);

    public double drainPower(double amount, boolean simulated);

    public double getEnergyBuffer();

    public double getEnergyBufferSize();

    public boolean isMoving();

    public boolean canMove();

    public boolean move(boolean simulated);

    public void update();

    public void onNeighborChange();

    public void onFirstTick();

    public void onUnload();

    public boolean onActivated(EntityPlayer player, int side, Vector3 hit);

    public void notifyChange();

    public void sendUpdate();

    public void writeMotor(NBTTagCompound tag);

    public void readMotor(NBTTagCompound tag);

    public void setNeedsRestart();

}
