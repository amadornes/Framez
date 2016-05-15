package com.amadornes.framez.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.amadornes.framez.api.compat.ICompatRegistry;
import com.amadornes.framez.api.modifier.IModifierRegistry;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.framez.api.movement.IFrameRenderData.IFrameBlockRenderData;
import com.amadornes.framez.api.movement.IMovementRegistry;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.trajectory.api.vec.BlockPos;

public interface IFramezApi {

    public ICompatRegistry compat();

    public IMovementRegistry movementRegistry();

    public IModifierRegistry modifierRegistry();

    public IFramezWrench getWrench(ItemStack stack);

    public IFrame wrapFrameBlock(World world, BlockPos position, IFrameBlock block);

    public IFrameRenderData wrapFrameBlockRenderData(IBlockAccess world, BlockPos position, IFrameBlockRenderData data);

}
