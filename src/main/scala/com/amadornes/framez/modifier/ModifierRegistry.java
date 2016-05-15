package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.modifier.IModifierRegistry;
import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.wrench.IFramePartHandler;
import com.amadornes.framez.util.ComparatorModifierType;

public class ModifierRegistry implements IModifierRegistry {

    public static final ModifierRegistry instance = new ModifierRegistry();

    public List<IFrameSideModifier> frameSideModifiers = new ArrayList<IFrameSideModifier>();
    public List<IFrameMaterial> frameMaterials = new ArrayList<IFrameMaterial>();
    public List<IFramePartHandler> framePartHandlers = new ArrayList<IFramePartHandler>();
    public List<IMotorModifier> motorModifiers = new ArrayList<IMotorModifier>();
    public List<IMotorUpgrade> motorUpgrades = new ArrayList<IMotorUpgrade>();

    private List<List<IMotorModifier>> motorCombinations = new ArrayList<List<IMotorModifier>>();

    @Override
    public void registerFrameSideModifier(IFrameSideModifier modifier) {

        if (modifier == null)
            throw new NullPointerException("A mod attempted to register a null frame side modifier! Report this to the author!");
        if (frameSideModifiers.contains(modifier))
            throw new IllegalStateException(
                    "A mod attempted to register a frame side modifier that was already registered! Report this to the author!");
        if (findFrameSideModifier(modifier.getType()) != null)
            throw new IllegalStateException(
                    "A mod attempted to register a frame side modifier with a name that was already registered! Report this to the author!");

        frameSideModifiers.add(modifier);
    }

    @Override
    public void registerFrameMaterial(IFrameMaterial material) {

        if (material == null)
            throw new NullPointerException("A mod attempted to register a null frame material! Report this to the author!");
        if (frameMaterials.contains(material))
            throw new IllegalStateException(
                    "A mod attempted to register a frame material that was already registered! Report this to the author!");
        if (findFrameMaterial(material.getType()) != null)
            throw new IllegalStateException(
                    "A mod attempted to register a frame material with a name that was already registered! Report this to the author!");

        frameMaterials.add(material);
    }

    @Override
    public void registerFramePartHandler(IFramePartHandler handler) {

        if (handler == null)
            throw new NullPointerException("A mod attempted to register a frame part handler! Report this to the author!");
        if (framePartHandlers.contains(handler))
            throw new IllegalStateException(
                    "A mod attempted to register a frame part handler that was already registered! Report this to the author!");

        framePartHandlers.add(handler);
    }

    @Override
    public void registerMotorModifier(IMotorModifier modifier) {

        if (modifier == null)
            throw new NullPointerException("A mod attempted to register a null motor modifier! Report this to the author!");
        if (motorModifiers.contains(modifier))
            throw new IllegalStateException(
                    "A mod attempted to register a motor modifier that was already registered! Report this to the author!");
        if (findMotorModifier(modifier.getType()) != null)
            throw new IllegalStateException(
                    "A mod attempted to register a motor modifier with a name that was already registered! Report this to the author!");

        motorModifiers.add(modifier);
    }

    @Override
    public void registerMotorUpgrade(IMotorUpgrade upgrade) {

        if (upgrade == null)
            throw new NullPointerException("A mod attempted to register a null motor upgrade! Report this to the author!");
        if (motorUpgrades.contains(upgrade))
            throw new IllegalStateException(
                    "A mod attempted to register a motor upgrade that was already registered! Report this to the author!");
        if (findMotorUpgrade(upgrade.getType()) != null)
            throw new IllegalStateException(
                    "A mod attempted to register a motor upgrade with a name that was already registered! Report this to the author!");

        motorUpgrades.add(upgrade);
    }

    public IFrameSideModifier findFrameSideModifier(String type) {

        for (IFrameSideModifier m : frameSideModifiers)
            if (m.getType().equals(type))
                return m;
        return null;
    }

    public IFrameMaterial findFrameMaterial(String type) {

        for (IFrameMaterial m : frameMaterials)
            if (m.getType().equals(type))
                return m;
        return null;
    }

    public IMotorModifier findMotorModifier(String type) {

        for (IMotorModifier m : motorModifiers)
            if (m.getType().equals(type))
                return m;
        return null;
    }

    public IMotorUpgrade findMotorUpgrade(String type) {

        for (IMotorUpgrade m : motorUpgrades)
            if (m.getType().equals(type))
                return m;
        return null;
    }

    public void registerMotorCombination(String... modifiers) {

        List<IMotorModifier> l = new ArrayList<IMotorModifier>();
        for (String s : modifiers) {
            IMotorModifier m = findMotorModifier(s);
            if (m != null && !l.contains(m))
                l.add(m);
        }
        if (l.size() == 0)
            return;
        Collections.sort(l, new ComparatorModifierType());
        if (!motorCombinations.contains(l))
            motorCombinations.add(l);
    }

    public List<List<IMotorModifier>> getAllMotorCombinations() {

        return motorCombinations;
    }

}
