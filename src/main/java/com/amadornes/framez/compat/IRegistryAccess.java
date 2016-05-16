package com.amadornes.framez.compat;

import java.util.Collection;
import java.util.Iterator;

public interface IRegistryAccess<T> extends Iterable<T> {

    public Collection<T> getRegisteredObjects();

    @Override
    public Iterator<T> iterator();

    public boolean isModulePresent(String module);

    public boolean isModuleOffered(String module);

}
