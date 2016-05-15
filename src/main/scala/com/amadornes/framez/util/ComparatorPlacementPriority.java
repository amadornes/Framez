package com.amadornes.framez.util;

import java.util.Comparator;

import com.amadornes.framez.api.compat.IFramePlacementHandler;

public class ComparatorPlacementPriority implements Comparator<IFramePlacementHandler> {

    @Override
    public int compare(IFramePlacementHandler o1, IFramePlacementHandler o2) {

        return Integer.compare(o2.priority(), o1.priority());
    }

}
