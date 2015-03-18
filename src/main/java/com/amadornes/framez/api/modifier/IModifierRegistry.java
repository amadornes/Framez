package com.amadornes.framez.api.modifier;

import java.util.Collection;

public interface IModifierRegistry<T> {

    public void registerModifier(T modifier);

    public Collection<T> getRegisteredModifiers();

    public T findModifier(String type);

}
