package com.amadornes.framez.modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.movement.IFramezMotor;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;

public class MotorFactory {

    @SafeVarargs
    public static <T extends IMotor> T createMotor(Class<T> clazz, String identifier, Class<? extends JTrait<?>>... postTraits) {

        String mods = identifier.substring(identifier.indexOf("_") + 1);

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

            IMotorModifier mod = ModifierRegistry.instance.findMotorModifier(m);
            if (mod == null)
                continue;

            modifierList.add(mod);
        }

        return createMotor(clazz, modifierList, postTraits);
    }

    @SafeVarargs
    public static <T extends IMotor> T createMotor(Class<T> clazz, Collection<IMotorModifier> modifiers,
            Class<? extends JTrait<?>>... postTraits) {

        try {
            T motor = createMotorClass(clazz, modifiers, postTraits).newInstance();
            if (motor instanceof IFramezMotor)
                ((IFramezMotor) motor).init(modifiers.toArray(new IMotorModifier[modifiers.size()]));
            return motor;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SafeVarargs
    public static <T extends IMotor> Class<? extends T> createMotorClass(Class<T> clazz, Collection<IMotorModifier> modifiers,
            Class<? extends JTrait<?>>... postTraits) {

        List<Class<? extends JTrait<?>>> traits = new LinkedList<Class<? extends JTrait<?>>>();
        for (IMotorModifier m : modifiers) {
            if (m.getTraitClass() != null)
                traits.add(m.getTraitClass());
        }
        traits.addAll(Arrays.asList(postTraits));

        try {
            return MixinFactory.mixin(clazz, traits.toArray(new Class[] {}));
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
