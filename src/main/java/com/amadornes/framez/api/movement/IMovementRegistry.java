package com.amadornes.framez.api.movement;

public interface IMovementRegistry {

    public void registerMultiblockStructureSupplier(IMultiblockStructureSupplier supplier);

    public void registerStructureIssueSupplier(IStructureIssueSupplier supplier);

}
