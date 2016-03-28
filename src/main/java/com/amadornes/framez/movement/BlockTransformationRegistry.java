package com.amadornes.framez.movement;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.api.movement.IBlockTransformationHandler;
import com.amadornes.framez.api.movement.IBlockTransformationRegistry;
import com.google.common.base.Predicate;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public enum BlockTransformationRegistry implements IBlockTransformationRegistry {

    INSTANCE;

    public List<Pair<Pair<Integer, Predicate<Pair<IBlockAccess, BlockPos>>>, IBlockTransformationHandler>> handlers = //
            new LinkedList<Pair<Pair<Integer, Predicate<Pair<IBlockAccess, BlockPos>>>, IBlockTransformationHandler>>();

    private BlockTransformationRegistry() {

        registerTransformationHandler(new DefaultBlockTransformationHandler(), a -> true, Integer.MIN_VALUE);
    }

    @Override
    public void registerTransformationHandler(IBlockTransformationHandler handler, Predicate<Pair<IBlockAccess, BlockPos>> predicate,
            int priority) {

        handlers.add(Pair.of(Pair.of(priority, predicate), handler));
        handlers.sort((a, b) -> Integer.compare(b.getKey().getKey(), a.getKey().getKey()));
    }

}
