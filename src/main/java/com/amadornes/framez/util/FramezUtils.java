package com.amadornes.framez.util;

import java.util.Collection;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.movement.IFrame;

public class FramezUtils {

    public static boolean hasModifier(IFrame frame, IFrameModifier mod) {

        return hasModifier(frame.getModifiers(), mod);
    }

    public static boolean hasModifier(IFrame frame, Class<? extends IFrameModifier> mod) {

        return hasModifier(frame.getModifiers(), mod);
    }

    public static boolean hasModifier(IFrame frame, String mod) {

        return hasModifier(frame.getModifiers(), mod);
    }

    public static boolean hasModifier(Collection<? extends IFrameModifier> mods, IFrameModifier mod) {

        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.equals(mod))
                return true;

        return false;
    }

    public static boolean hasModifier(Collection<? extends IFrameModifier> mods, Class<? extends IFrameModifier> mod) {

        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.getClass().equals(mod))
                return true;

        return false;
    }

    public static boolean hasModifier(Collection<? extends IFrameModifier> mods, String mod) {

        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.getType().equals(mod))
                return true;

        return false;
    }

}
