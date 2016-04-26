package com.amadornes.framez.movement;

import java.util.Map;
import java.util.TreeMap;

import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovementRegistry;
import com.amadornes.framez.api.movement.IMultiblockStructureSupplier;
import com.amadornes.framez.api.movement.IStructureIssueSupplier;

public enum MovementRegistry implements IMovementRegistry {

    INSTANCE;

    static {
        INSTANCE.registerMovementHandler(new DefaultMovementHandler(), Integer.MIN_VALUE);
    }

    private final Map<Integer, IMovementHandler> movementHandlers = new TreeMap<Integer, IMovementHandler>((a, b) -> Integer.compare(b, a));

    @Override
    public void registerMovementHandler(IMovementHandler handler) {

        registerMovementHandler(handler, 0);
    }

    @Override
    public void registerMovementHandler(IMovementHandler handler, int priority) {

        movementHandlers.put(priority, handler);
    }

    @Override
    public void registerMultiblockStructureSupplier(IMultiblockStructureSupplier supplier) {

    }

    @Override
    public void registerStructureIssueSupplier(IStructureIssueSupplier supplier) {

    }

}
