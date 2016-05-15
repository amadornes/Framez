package com.amadornes.framez.modifier;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.movement.IFramezFrame;
import com.amadornes.jtraits.JTrait;
import com.amadornes.jtraits.MixinFactory;

public class FrameFactory {

    public static String getIdentifier(String prefix, IFrameMaterial material) {

        StringBuilder sb = new StringBuilder();

        if (prefix != null)
            sb.append(prefix).append("_");
        sb.append(material.getType());

        return sb.toString();
    }

    public static IFrameMaterial getMaterial(String identifier) {

        return ModifierRegistry.instance.findFrameMaterial(identifier.substring(identifier.indexOf("_") + 1));
    }

    @SafeVarargs
    public static <T extends IFrame> T createFrame(Class<T> clazz, IFrameMaterial material, Class<? extends JTrait<?>>... extraTraits) {

        try {
            T frame = createFrameClass(clazz, extraTraits).newInstance();
            if (frame instanceof IFramezFrame)
                ((IFramezFrame) frame).init(material);
            return frame;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SafeVarargs
    public static <T extends IFrame> Class<? extends T> createFrameClass(Class<T> clazz, Class<? extends JTrait<?>>... extraTraits) {

        try {
            return MixinFactory.mixin(clazz, extraTraits);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
