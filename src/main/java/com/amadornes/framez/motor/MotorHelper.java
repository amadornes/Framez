package com.amadornes.framez.motor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class MotorHelper {

    public static boolean addUpgrade(TileMotor motor, IMotorUpgradeFactory upgradeFactory, int slot, ItemStack stack, boolean simulate) {

        if (!upgradeFactory.canApply(motor.getSafeReference(), stack, Framez.proxy.getPlayer())) return false;
        motor.setUpgrade(slot, upgradeFactory.createUpgrade(motor.getSafeReference(), slot), stack.copy().splitStack(1));
        if (upgradeFactory.getTrait() != null && !simulate) {
            TileMotor newMotor = mixin(motor.getBaseClass(), motor.upgrades);
            newMotor.copyFrom(motor);
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
            TileMotor newMotor = mixin(motor.getBaseClass(), motor.upgrades);
            newMotor.copyFrom(motor);
            motor.reference.set(newMotor);
            motor.getWorld().removeTileEntity(motor.getPos());
            motor.getWorld().setTileEntity(motor.getPos(), newMotor);
        }
    }

    private static <T extends TileMotor> T mixin(Class<T> clazz, Pair<IMotorUpgrade, ItemStack>[] upgrades) {

        try {
            List<Class<? extends JTrait<? extends IMotor>>> allTraits = new LinkedList<Class<? extends JTrait<? extends IMotor>>>();
            for (Pair<IMotorUpgrade, ItemStack> upgrade : upgrades) {
                if (upgrade != null) {
                    IMotorUpgradeFactory upgradeFactory = MotorRegistry.INSTANCE.upgrades.get(upgrade.getLeft().getType());
                    if (upgradeFactory.getTrait() != null) allTraits.add(upgradeFactory.getTrait());
                }
            }
            return MixinFactory.mixin(clazz, allTraits.toArray(new Class[allTraits.size()])).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<BlockPos> findMovedBlocks(IBlockAccess world, BlockPos pos, EnumFacing... directions) {

        Set<BlockPos> blocks = new HashSet<BlockPos>();
        for (EnumFacing dir : directions)
            blocks.add(pos.offset(dir));
        return blocks;
    }

}
