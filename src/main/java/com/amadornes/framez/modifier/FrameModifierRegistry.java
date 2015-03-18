package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.modifier.IFrameModifierRegistry;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.util.SorterElementCount;
import com.amadornes.framez.util.SorterModifierType;

public class FrameModifierRegistry implements IFrameModifierRegistry {

    private static final FrameModifierRegistry instance = new FrameModifierRegistry();

    public static FrameModifierRegistry instance() {

        return instance;
    }

    private List<IFrameModifier> registered = new ArrayList<IFrameModifier>();
    private Map<Class<? extends IFrame>, List<List<IFrameModifier>>> combinations = new HashMap<Class<? extends IFrame>, List<List<IFrameModifier>>>();

    @Override
    public void registerModifier(IFrameModifier modifier) {

        if (modifier == null)
            return;
        if (findModifier(modifier.getType()) != null)
            return;

        registered.add(modifier);
    }

    @Override
    public Collection<IFrameModifier> getRegisteredModifiers() {

        return registered;
    }

    @Override
    public IFrameModifier findModifier(String type) {

        for (IFrameModifier m : registered)
            if (m.getType().equals(type))
                return m;

        return null;
    }

    public Collection<List<IFrameModifier>> getAllCombinations(Class<? extends IFrame> frameClazz) {

        // if (combinations.containsKey(frameClazz))
        // return combinations.get(frameClazz);

        List<List<IFrameModifier>> combinations = new ArrayList<List<IFrameModifier>>();

        for (IFrameModifier p : registered) {
            List<List<IFrameModifier>> pos = new ArrayList<List<IFrameModifier>>();
            pos.add(Arrays.asList(new IFrameModifier[] { p }));
            addModifiers(pos, Arrays.asList(new IFrameModifier[] { p }));

            for (List<IFrameModifier> l : pos) {
                boolean found = false;

                for (List<IFrameModifier> l2 : combinations) {
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

    private void addModifiers(List<List<IFrameModifier>> possibilities, List<IFrameModifier> current) {

        for (IFrameModifier p : registered) {
            if (current.contains(p))
                continue;
            if (!areCompatible(current, p))
                continue;

            List<IFrameModifier> l = new ArrayList<IFrameModifier>();
            l.addAll(current);
            l.add(p);
            possibilities.add(l);

            addModifiers(possibilities, l);
        }
    }

    private boolean areCompatible(List<IFrameModifier> mods, IFrameModifier m) {

        for (IFrameModifier mod : mods)
            if (!mod.isCompatibleWith(m) || !m.isCompatibleWith(mod))
                return false;

        return true;
    }

    private void removeDuplicates(List<List<IFrameModifier>> possibilities) {

        List<List<IFrameModifier>> removed = new ArrayList<List<IFrameModifier>>();

        for (List<IFrameModifier> l1 : possibilities) {
            if (removed.contains(l1))
                continue;
            for (List<IFrameModifier> l2 : possibilities) {
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
