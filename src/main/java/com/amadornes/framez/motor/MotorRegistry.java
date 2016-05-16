package com.amadornes.framez.motor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorExtension;
import com.amadornes.framez.api.motor.IMotorRegistry;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.api.motor.IMotorVariable;
import com.amadornes.framez.motor.upgrade.UpgradeFactoryBase;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.jtraits.JTrait;
import com.google.common.base.Function;

import net.minecraft.util.ResourceLocation;

public enum MotorRegistry implements IMotorRegistry {

    INSTANCE;

    public Map<ResourceLocation, IMotorUpgradeFactory> upgrades = new LinkedHashMap<ResourceLocation, IMotorUpgradeFactory>();
    public Map<String, IMotorUpgradeFactory> internalUpgrades = new LinkedHashMap<String, IMotorUpgradeFactory>();
    public Map<ResourceLocation, MotorExtension> extensions = new LinkedHashMap<ResourceLocation, MotorExtension>();

    @Override
    public void registerUpgrade(ResourceLocation type, IMotorUpgradeFactory factory) {

        upgrades.put(type, factory);
    }

    public void registerUpgradeInternal(ResourceLocation type, UpgradeFactoryBase factory) {

        registerUpgrade(type, factory);
        internalUpgrades.put(type.getResourcePath(), factory);
    }

    @Override
    public void registerExtension(ResourceLocation type, Function<DynamicReference<? extends IMotor>, IMotorExtension> extensionSupplier) {

        registerExtension(type, extensionSupplier, null);
    }

    @Override
    public void registerExtension(ResourceLocation type, Function<DynamicReference<? extends IMotor>, IMotorExtension> extensionSupplier,
            Class<? extends JTrait<IMotor>> trait) {

        extensions.put(type, new MotorExtension(extensionSupplier, trait));
    }

    @Override
    public <T> IMotorVariable<T> createVariable() {

        return new SimpleMotorVariable<T>();
    }

    @Override
    public <T> IMotorVariable<T> createVariable(String unlocalizedName, Function<T, String> toString) {

        return new SimpleMotorVariable<T>(unlocalizedName, toString);
    }

    @Override
    public IMotorVariable<Double> varPowerStored() {

        return TileMotor.POWER_STORED;
    }

    @Override
    public IMotorVariable<Double> varPowerStorageSize() {

        return TileMotor.POWER_STORAGE_SIZE;
    }

    @Override
    public IMotorVariable<Integer> varMovementTime() {

        return TileMotor.MOVEMENT_TIME;
    }

    public static final class MotorExtension {

        private final Function<DynamicReference<? extends IMotor>, IMotorExtension> supplier;
        private final Class<? extends JTrait<IMotor>> trait;

        public MotorExtension(Function<DynamicReference<? extends IMotor>, IMotorExtension> supplier,
                Class<? extends JTrait<IMotor>> trait) {

            this.supplier = supplier;
            this.trait = trait;
        }

        public IMotorExtension instantiate(DynamicReference<? extends IMotor> motor) {

            return supplier.apply(motor);
        }

        public Class<? extends JTrait<IMotor>> getTrait() {

            return trait;
        }

    }

}
