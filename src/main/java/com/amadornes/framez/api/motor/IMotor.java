package com.amadornes.framez.api.motor;

import java.util.Map.Entry;

import com.amadornes.framez.api.DynamicReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMotor {

    public World getMotorWorld();

    public BlockPos getMotorPos();

    public DynamicReference<? extends IMotor> getSafeReference();

    public boolean isMoving();

    public boolean canMove();

    public DynamicReference<Boolean> move();

    public IMotorTrigger getTrigger(EnumMotorAction action);

    public int getUpgradeSlots();

    public Entry<IMotorUpgrade, ItemStack> getUpgrade(int slot);

    public boolean checkStatus(EnumMotorStatus status);

    public <T> T getVariable(IMotorVariable<T> variable);

}
