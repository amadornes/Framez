package com.amadornes.framez.util;

import java.util.Comparator;

import uk.co.qmunity.lib.util.AlphanumComparator;

import com.amadornes.framez.api.modifier.IFrameModifierMaterial;
import com.amadornes.framez.api.modifier.IModifier;

public class SorterModifierType implements Comparator<IModifier<?>> {

    @Override
    public int compare(IModifier<?> o1, IModifier<?> o2) {

        if ((o1 instanceof IFrameModifierMaterial) != (o2 instanceof IFrameModifierMaterial))
            return Boolean.compare(!(o1 instanceof IFrameModifierMaterial), !(o2 instanceof IFrameModifierMaterial));

        return new AlphanumComparator().compare(o1.getType(), o2.getType());
    }

}
