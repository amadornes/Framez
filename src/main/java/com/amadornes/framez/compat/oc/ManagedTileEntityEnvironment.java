package com.amadornes.framez.compat.oc;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.ManagedEnvironment;

public class ManagedTileEntityEnvironment<T> extends ManagedEnvironment {

    protected final T te;

    public ManagedTileEntityEnvironment(T tileEntity, String name) {

        this.te = tileEntity;
        setNode(Network.newNode(this, Visibility.Network).withComponent(name).create());
    }
}
