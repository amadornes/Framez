package com.amadornes.framez.api.modifier;

import java.util.Collection;

import com.amadornes.jtraits.ITrait;

public interface IModifier<T> {

    public String getType();

    public boolean isCompatibleWith(T mod);

    public boolean isValidCombination(Collection<T> combination);

    public Class<? extends ITrait> getTraitClass();

}
