package com.amadornes.framez.part;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;

public class RegisterParts implements IPartFactory {

    public static void init() {

        MultiPartRegistry.registerParts(new RegisterParts(), new String[] { ModInfo.MODID + "." + References.Names.Registry.FRAME });
    }

    @Override
    public TMultiPart createPart(String type, boolean client) {

        if (type == ModInfo.MODID + "." + References.Names.Registry.FRAME)
            return new PartFrame();

        return null;
    }

}
