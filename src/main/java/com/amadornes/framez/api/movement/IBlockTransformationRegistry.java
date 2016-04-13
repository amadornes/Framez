package com.amadornes.framez.api.movement;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockTransformationRegistry {

    public void registerTransformationHandler(IBlockTransformationHandler handler, Predicate<Pair<IBlockAccess, BlockPos>> predicate,
            int priority);

}
