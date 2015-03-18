package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amadornes.framez.api.modifier.IFrameModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.jtraits.ClassFactory;
import com.amadornes.jtraits.ITrait;

public class FrameFactory {

    public static <T extends IFrame> T createFrame(Class<T> clazz, String identifier) {

        String mods = identifier.substring(identifier.lastIndexOf("_") + 1);

        List<IFrameModifier> modifierList = new ArrayList<IFrameModifier>();

        int id = -1;
        while ((id = mods.indexOf("$")) >= 0 || mods.length() > 0) {
            String m = mods;
            if (id >= 0) {
                m = mods.substring(0, id);
                mods = mods.substring(id + 1);
            } else {
                mods = "";
            }

            IFrameModifier mod = FrameModifierRegistry.instance().findModifier(m);
            if (mod == null)
                continue;

            modifierList.add(mod);
        }

        return createFrame(clazz, modifierList);
    }

    @SuppressWarnings("unchecked")
    public static <T extends IFrame> T createFrame(Class<T> clazz, List<IFrameModifier> mods) {

        List<Class<? extends ITrait>> traits = new ArrayList<Class<? extends ITrait>>();
        for (IFrameModifier m : mods)
            if (m.getTraitClass() != null)
                traits.add(m.getTraitClass());

        try {
            T frame = (T) ClassFactory.createClass(clazz, traits.toArray(new Class[] {})).newInstance();
            frame.getModifiers().addAll(mods);
            return frame;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getIdentifier(String prefix, Collection<IFrameModifier> mods) {

        StringBuilder sb = new StringBuilder();

        if (prefix != null)
            sb.append(prefix + "_");

        if (mods != null)
            for (IFrameModifier m : mods)
                sb.append(m.getType() + "$");

        return sb.toString().substring(0, sb.length() - 1);
    }
}
