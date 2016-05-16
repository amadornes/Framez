package com.amadornes.framez.api.motor;

import java.util.Map.Entry;

import com.amadornes.framez.api.DynamicReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IMotor {

    @CapabilityInject(IMotor.class)
    public static final Capability<IMotor> CAPABILITY_MOTOR = null;

    public World getMotorWorld();

    public BlockPos getMotorPos();

    public DynamicReference<? extends IMotor> getSafeReference();

    public boolean canMove(IMotorAction action);

    public DynamicReference<Boolean> move(IMotorAction action);

    public int getUpgradeSlots();

    public Entry<IMotorUpgrade, ItemStack> getUpgrade(int slot);

    public boolean checkStatus(EnumMotorStatus status);

    public <T> T getVariable(IMotorVariable<T> variable);

    public boolean hasCapability(Capability<?> capability, EnumFacing facing);

    public <T> T getCapability(Capability<T> capability, EnumFacing facing);

    public <T extends IMotorExtension> T getExtension(ResourceLocation name, Class<T> type);

}
