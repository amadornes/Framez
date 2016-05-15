package com.amadornes.framez.compat.oc;

import li.cil.oc.api.driver.EnvironmentAware;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.compat.upgrade.ComputerizedUpgrade;
import com.amadornes.framez.tile.TileMotor;

public class DriverMotor extends DriverTileEntity implements EnvironmentAware {

    @Override
    public Class<?> getTileEntityClass() {

        return TileMotor.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return null;
        TileMotor te = (TileMotor) tile;

        if (FramezCompatConfig.oc_require_upgrade) {
            for (int i = 0; i < 7; i++) {
                IMotorUpgradeData data = te.getUpgrades()[i];
                if (data != null && data.getUpgrade().equals(ComputerizedUpgrade.upgrade))
                    return new EnvironmentMotor(te);
            }
        } else {
            return new EnvironmentMotor(te);
        }

        return null;
    }

    @Override
    public Class<? extends Environment> providedEnvironment(ItemStack stack) {

        if (stack != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockMotor)
            return EnvironmentMotor.class;
        return null;
    }

}