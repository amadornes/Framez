package com.amadornes.framez.modifier.frame;

import java.util.Collection;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.ref.References;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

public class FrameModifierSimple implements IFrameModifier {

    @Override
    public String getType() {

        return References.Modifier.SIMPLE;
    }

    @Override
    public boolean isCompatibleWith(IFrameModifier mod) {

        return true;
    }

    @Override
    public boolean isValidCombination(Collection<IFrameModifier> combination) {

        if (combination.size() == 1)
            return false;

        for (IFrameModifier m : combination)
            if (m == this)
                return true;

        return false;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TFrameSimple.class;
    }

    public static abstract class TFrameSimple extends JTrait<IFrame> implements IFrame {

        @Override
        public boolean is2D() {

            return true;
        }

    }

}
