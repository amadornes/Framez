package com.amadornes.framez.api.modifier;

import java.util.Collection;

import com.amadornes.jtraits.JTrait;

public interface IModifier<MOD> {

    public String getType();

    public boolean isCompatibleWith(MOD modifier);

    public boolean isCombinationValid(Collection<MOD> modifiers);

    public static interface IModifierTrait<MOD, OBJ> extends IModifier<MOD> {

        public Class<? extends JTrait<? extends OBJ>> getTraitClass();
    }

}
