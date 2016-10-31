package com.amadornes.framez.motor;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.motor.MotorRegistry.MotorExtension;
import com.amadornes.framez.motor.logic.IMotorLogic;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;

import net.minecraft.item.ItemStack;

public class MotorHelper {

    public static boolean addUpgrade(TileMotor motor, IMotorUpgradeFactory upgradeFactory, int slot, ItemStack stack, boolean simulate) {

        if (!upgradeFactory.canApply(motor.getSafeReference(), stack, Framez.proxy.getPlayer())) {
            return false;
        }
        motor.setUpgrade(slot, upgradeFactory.createUpgrade(motor.getSafeReference(), slot), stack.copy().splitStack(1));
        if (upgradeFactory.getTrait() != null && !simulate) {
            TileMotor newMotor = mixin(motor.getBaseClass(), motor.upgrades, motor);
            motor.reference.set(newMotor);
            motor.getWorld().removeTileEntity(motor.getPos());
            motor.getWorld().setTileEntity(motor.getPos(), newMotor);
        }
        return true;
    }

    public static void removeUpgrade(TileMotor motor, int slot) {

        IMotorUpgrade upgrade = motor.getUpgrade(slot).getKey();
        IMotorUpgradeFactory upgradeFactory = MotorRegistry.INSTANCE.upgrades.get(upgrade.getType());
        motor.setUpgrade(slot, null, null);
        if (upgradeFactory.getTrait() != null) {
            TileMotor newMotor = mixin(motor.getBaseClass(), motor.upgrades, motor);
            motor.reference.set(newMotor);
            motor.getWorld().removeTileEntity(motor.getPos());
            motor.getWorld().setTileEntity(motor.getPos(), newMotor);
        }
    }

    private static <T extends TileMotor> T mixin(Class<T> clazz, Pair<IMotorUpgrade, ItemStack>[] upgrades, TileMotor motor) {

        try {
            Set<Class<? extends JTrait<? extends IMotor>>> allTraits = new LinkedHashSet<Class<? extends JTrait<? extends IMotor>>>();
            for (MotorExtension extension : MotorRegistry.INSTANCE.extensions.values()) {
                if (extension.getTrait() != null) {
                    allTraits.add(extension.getTrait());
                }
            }
            for (Pair<IMotorUpgrade, ItemStack> upgrade : upgrades) {
                if (upgrade != null) {
                    IMotorUpgradeFactory upgradeFactory = MotorRegistry.INSTANCE.upgrades.get(upgrade.getLeft().getType());
                    if (upgradeFactory.getTrait() != null) {
                        allTraits.add(upgradeFactory.getTrait());
                    }
                }
            }
            return MixinFactory.mixin(clazz, allTraits.toArray(new Class[allTraits.size()]))
                    .getConstructor(TileMotor.class, IMotorLogic.class).newInstance(motor, motor.getLogic());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
