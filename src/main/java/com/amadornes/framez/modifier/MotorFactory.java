package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.ClassFactory;
import com.amadornes.jtraits.ITrait;

public class MotorFactory {

    public static <T extends IMotor> T createMotor(Class<T> clazz, String identifier) {

        String mods = identifier.substring(identifier.lastIndexOf("_") + 1);

        List<IMotorModifier> modifierList = new ArrayList<IMotorModifier>();

        int id = -1;
        while ((id = mods.indexOf("$")) >= 0 || mods.length() > 0) {
            String m = mods;
            if (id >= 0) {
                m = mods.substring(0, id);
                mods = mods.substring(id + 1);
            } else {
                mods = "";
            }

            IMotorModifier mod = MotorModifierRegistry.instance().findModifier(m);
            if (mod == null)
                continue;

            modifierList.add(mod);
        }

        return createMotor(clazz, modifierList);
    }

    public static <T extends IMotor> T createMotor(Class<T> clazz, List<IMotorModifier> mods) {

        try {
            T frame = createMotorClass(clazz, mods).newInstance();
            frame.getModifiers().addAll(mods);
            return frame;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IMotor> Class<T> createMotorClass(Class<T> clazz, List<IMotorModifier> mods) {

        List<Class<? extends ITrait>> traits = new ArrayList<Class<? extends ITrait>>();
        for (IMotorModifier m : mods)
            if (m.getTraitClass() != null)
                traits.add(m.getTraitClass());

        try {
            return ClassFactory.createClass(clazz, traits.toArray(new Class[] {}));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getIdentifier(String prefix, Collection<IMotorModifier> mods) {

        StringBuilder sb = new StringBuilder();

        if (prefix != null)
            sb.append(prefix + "_");

        if (mods != null)
            for (IMotorModifier m : mods)
                sb.append(m.getType() + "$");

        return sb.toString().substring(0, sb.length() - 1);
    }
}
