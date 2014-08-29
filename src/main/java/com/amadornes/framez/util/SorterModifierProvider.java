package com.amadornes.framez.util;

import java.util.Comparator;

import com.amadornes.framez.api.IFrameModifierProvider;

public class SorterModifierProvider implements Comparator<IFrameModifierProvider> {

    @Override
    public int compare(IFrameModifierProvider o1, IFrameModifierProvider o2) {

        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

}
