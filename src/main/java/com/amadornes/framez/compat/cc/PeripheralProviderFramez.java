package com.amadornes.framez.compat.cc;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.tile.TileMotor;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

public class PeripheralProviderFramez implements IPeripheralProvider {

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMotor)
            return new PeripheralMotor((TileMotor) te);

        return null;
    }

}
