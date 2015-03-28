package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierRegistry;
import com.amadornes.framez.util.SorterModifierType;

public class MotorModifierRegistry implements IMotorModifierRegistry {

    private static final MotorModifierRegistry instance = new MotorModifierRegistry();

    public static MotorModifierRegistry instance() {

        return instance;
    }

    private List<IMotorModifier> registered = new ArrayList<IMotorModifier>();
    private List<List<IMotorModifier>> combinations = new ArrayList<List<IMotorModifier>>();

    @Override
    public void registerModifier(IMotorModifier modifier) {

        if (modifier == null)
            throw new RuntimeException("Attempted to register a null motor modifier");
        if (modifier.getType() == null)
            throw new RuntimeException("Attempted to register a motor modifier with a null identifier");
        if (findModifier(modifier.getType()) != null)
            throw new RuntimeException("Attempted to register a motor modifier that has already been registered");

        registered.add(modifier);
    }

    @Override
    public Collection<IMotorModifier> getRegisteredModifiers() {

        return registered;
    }

    @Override
    public IMotorModifier findModifier(String type) {

        for (IMotorModifier m : registered)
            if (m.getType().equals(type))
                return m;

        return null;
    }

    public void registerCombination(String... modifiers) {

        List<IMotorModifier> l = new ArrayList<IMotorModifier>();
        for (String s : modifiers) {
            IMotorModifier m = findModifier(s);
            if (m != null && !l.contains(m))
                l.add(m);
        }
        if (l.size() == 0)
            return;
        Collections.sort(l, new SorterModifierType());
        if (!combinations.contains(l))
            combinations.add(l);
    }

    public List<List<IMotorModifier>> getAllCombinations() {

        return combinations;
    }
}
