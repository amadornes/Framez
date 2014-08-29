package com.amadornes.framez.util;

import java.util.Comparator;

import com.amadornes.framez.api.IFrameModifier;

public class SorterModifier implements Comparator<IFrameModifier> {

    @Override
    public int compare(IFrameModifier o1, IFrameModifier o2) {

        return o1.getIdentifier().compareTo(o2.getIdentifier());
    }

}
