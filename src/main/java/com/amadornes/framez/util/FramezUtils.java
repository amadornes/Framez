package com.amadornes.framez.util;

import java.util.Collection;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.movement.IFrame;

public class FramezUtils {

    public static boolean hasModifier(IFrame frame, IFrameModifier mod) {

        Collection<IFrameModifier> mods = frame.getModifiers();
        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.equals(mod))
                return true;

        return false;
    }

    public static boolean hasModifier(IFrame frame, Class<? extends IFrameModifier> mod) {

        Collection<IFrameModifier> mods = frame.getModifiers();
        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.getClass().equals(mod))
                return true;

        return false;
    }

    public static boolean hasModifier(IFrame frame, String mod) {

        Collection<IFrameModifier> mods = frame.getModifiers();
        if (mods == null)
            return false;

        for (IFrameModifier m : mods)
            if (m.getType().equals(mod))
                return true;

        return false;
    }

}
