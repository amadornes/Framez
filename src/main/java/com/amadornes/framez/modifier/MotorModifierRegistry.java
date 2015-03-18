package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierRegistry;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.util.SorterElementCount;
import com.amadornes.framez.util.SorterModifierType;

public class MotorModifierRegistry implements IMotorModifierRegistry {

    private static final MotorModifierRegistry instance = new MotorModifierRegistry();

    public static MotorModifierRegistry instance() {

        return instance;
    }

    private List<IMotorModifier> registered = new ArrayList<IMotorModifier>();
    private Map<Class<? extends IMotor>, List<List<IMotorModifier>>> combinations = new HashMap<Class<? extends IMotor>, List<List<IMotorModifier>>>();

    @Override
    public void registerModifier(IMotorModifier modifier) {

        if (modifier == null)
            return;
        if (findModifier(modifier.getType()) != null)
            return;

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

    public Collection<List<IMotorModifier>> getAllCombinations(Class<? extends IMotor> frameClazz) {

        // if (combinations.containsKey(frameClazz))
        // return combinations.get(frameClazz);

        List<List<IMotorModifier>> combinations = new ArrayList<List<IMotorModifier>>();

        for (IMotorModifier p : registered) {
            List<List<IMotorModifier>> pos = new ArrayList<List<IMotorModifier>>();
            pos.add(Arrays.asList(new IMotorModifier[] { p }));
            addModifiers(pos, Arrays.asList(new IMotorModifier[] { p }));

            for (List<IMotorModifier> l : pos) {
                boolean found = false;

                for (List<IMotorModifier> l2 : combinations) {
                    Collections.sort(l, new SorterModifierType());
                    Collections.sort(l2, new SorterModifierType());
                    found = l.equals(l2);
                    if (found)
                        break;
                }

                if (!found)
                    combinations.add(l);
            }
        }

        removeDuplicates(combinations);
        Collections.sort(combinations, new SorterElementCount());

        this.combinations.put(frameClazz, combinations);
        return combinations;
    }

    private void addModifiers(List<List<IMotorModifier>> possibilities, List<IMotorModifier> current) {

        for (IMotorModifier p : registered) {
            if (current.contains(p))
                continue;
            if (!areCompatible(current, p))
                continue;

            List<IMotorModifier> l = new ArrayList<IMotorModifier>();
            l.addAll(current);
            l.add(p);
            possibilities.add(l);

            addModifiers(possibilities, l);
        }
    }

    private boolean areCompatible(List<IMotorModifier> mods, IMotorModifier m) {

        for (IMotorModifier mod : mods)
            if (!mod.isCompatibleWith(m) || !m.isCompatibleWith(mod))
                return false;

        return true;
    }

    private void removeDuplicates(List<List<IMotorModifier>> possibilities) {

        List<List<IMotorModifier>> removed = new ArrayList<List<IMotorModifier>>();

        for (List<IMotorModifier> l1 : possibilities) {
            if (removed.contains(l1))
                continue;
            for (List<IMotorModifier> l2 : possibilities) {
                if (removed.contains(l2))
                    continue;
                if (l1 == l2)
                    continue;
                if (l1.size() != l2.size())
                    continue;
                Collections.sort(l1, new SorterModifierType());
                Collections.sort(l2, new SorterModifierType());
                boolean error = false;
                for (int i = 0; i < l1.size(); i++) {
                    if (l1 != l2) {
                        error = true;
                        break;
                    }
                }
                if (!error)
                    removed.add(l2);
            }
        }

        possibilities.removeAll(removed);
    }
}
