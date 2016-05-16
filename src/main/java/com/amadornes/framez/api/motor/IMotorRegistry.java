package com.amadornes.framez.api.motor;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.jtraits.JTrait;
import com.google.common.base.Function;

import net.minecraft.util.ResourceLocation;

public interface IMotorRegistry {

    public void registerUpgrade(ResourceLocation type, IMotorUpgradeFactory factory);

    public void registerExtension(ResourceLocation type, Function<DynamicReference<? extends IMotor>, IMotorExtension> extensionSupplier);

    public void registerExtension(ResourceLocation type, Function<DynamicReference<? extends IMotor>, IMotorExtension> extensionSupplier,
            Class<? extends JTrait<IMotor>> trait);

    public <T> IMotorVariable<T> createVariable();

    public <T> IMotorVariable<T> createVariable(String unlocalizedName, Function<T, String> toString);

    public IMotorVariable<Double> varPowerStored();

    public IMotorVariable<Double> varPowerStorageSize();

    public IMotorVariable<Integer> varMovementTime();

}
