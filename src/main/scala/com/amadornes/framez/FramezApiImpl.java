package com.amadornes.framez;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.amadornes.framez.api.IFramezApi;
import com.amadornes.framez.api.compat.ICompatRegistry;
import com.amadornes.framez.api.modifier.IModifierRegistry;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.framez.api.movement.IFrameRenderData.IFrameBlockRenderData;
import com.amadornes.framez.api.movement.IMovementRegistry;
import com.amadornes.framez.api.wrench.IFramezWrench;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.movement.FrameBlockRenderDataWrapper;
import com.amadornes.framez.movement.FrameBlockWrapper;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.trajectory.api.vec.BlockPos;

public class FramezApiImpl implements IFramezApi {

    @Override
    public ICompatRegistry compat() {

        return CompatRegistryImpl.instance;
    }

    @Override
    public IMovementRegistry movementRegistry() {

        return MovementRegistry.instance;
    }

    @Override
    public IModifierRegistry modifierRegistry() {

        return ModifierRegistry.instance;
    }

    @Override
    public IFramezWrench getWrench(ItemStack stack) {

        if (stack == null)
            return null;
        if (stack.getItem() instanceof IFramezWrench)
            return (IFramezWrench) stack.getItem();
        IFramezWrench moddedWrench = compat().getModdedWrench(stack);
        if (moddedWrench != null)
            return moddedWrench;
        return null;
    }

    @Override
    public IFrame wrapFrameBlock(World world, BlockPos position, IFrameBlock block) {

        return new FrameBlockWrapper(world, position, block);
    }

    @Override
    public IFrameRenderData wrapFrameBlockRenderData(IBlockAccess world, BlockPos position, IFrameBlockRenderData data) {

        return new FrameBlockRenderDataWrapper(world, position, data);
    }

}
