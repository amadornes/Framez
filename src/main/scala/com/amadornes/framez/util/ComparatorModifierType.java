package com.amadornes.framez.util;

import java.util.Comparator;

import com.amadornes.framez.api.modifier.IModifier;

public class ComparatorModifierType implements Comparator<IModifier<?>> {

    @Override
    public int compare(IModifier<?> o1, IModifier<?> o2) {

        return new ComparatorAlphanum().compare(o1.getType(), o2.getType());
    }

}
