package com.amadornes.trajectory.util;

import java.util.Comparator;

import com.amadornes.trajectory.api.IBlockDescriptionProvider;
import com.amadornes.trajectory.api.IBlockDescriptionProvider.IPrioritisedBlockDescriptionProvider;

public class ComparatorDescriptionPriority implements Comparator<IBlockDescriptionProvider> {

    @Override
    public int compare(IBlockDescriptionProvider a, IBlockDescriptionProvider b) {

        return Integer.compare(b instanceof IPrioritisedBlockDescriptionProvider ? ((IPrioritisedBlockDescriptionProvider) b).priority()
                : 0, a instanceof IPrioritisedBlockDescriptionProvider ? ((IPrioritisedBlockDescriptionProvider) a).priority() : 0);
    }

}
