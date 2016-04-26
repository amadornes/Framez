package com.amadornes.framez.api.movement;

public interface IMovementRegistry {

    public void registerMovementHandler(IMovementHandler handler);

    public void registerMovementHandler(IMovementHandler handler, int priority);

    public void registerMultiblockStructureSupplier(IMultiblockStructureSupplier supplier);

    public void registerStructureIssueSupplier(IStructureIssueSupplier supplier);

}
