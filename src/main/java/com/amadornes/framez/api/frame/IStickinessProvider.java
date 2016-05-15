package com.amadornes.framez.api.frame;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IStickinessProvider {

    public ISticky getSticky(IBlockAccess world, BlockPos pos, EnumFacing face);

    public IStickable getStickable(IBlockAccess world, BlockPos pos, EnumFacing face);

}
