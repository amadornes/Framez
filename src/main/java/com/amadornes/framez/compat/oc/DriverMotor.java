package com.amadornes.framez.compat.oc;

import li.cil.oc.api.driver.EnvironmentAware;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.tile.TileMotor;

public class DriverMotor extends DriverTileEntity implements EnvironmentAware {

    @Override
    public Class<?> getTileEntityClass() {

        return TileMotor.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileMotor)
            return new EnvironmentMotor((TileMotor) te);
        return null;
    }

    @Override
    public Class<? extends Environment> providedEnvironment(ItemStack stack) {

        if (stack != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockMotor)
            return EnvironmentMotor.class;
        return null;
    }

}
