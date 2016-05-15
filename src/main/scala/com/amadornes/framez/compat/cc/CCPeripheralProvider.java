package com.amadornes.framez.compat.cc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.compat.FramezCompatConfig;
import com.amadornes.framez.compat.upgrade.ComputerizedUpgrade;
import com.amadornes.framez.tile.TileMotor;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class CCPeripheralProvider implements IPeripheralProvider {

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return null;
        TileMotor te = (TileMotor) tile;

        if (FramezCompatConfig.cc_require_upgrade) {
            for (int i = 0; i < 7; i++) {
                IMotorUpgradeData data = te.getUpgrades()[i];
                if (data != null && data.getUpgrade().equals(ComputerizedUpgrade.upgrade))
                    return new CCPeripheralMotor(te);
            }
        } else {
            return new CCPeripheralMotor(te);
        }

        return null;
    }

}
