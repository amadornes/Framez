package com.amadornes.framez.part;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.amadornes.framez.ref.References;

public class RegisterParts implements IPartFactory {

    public static void init() {

        MultiPartRegistry.registerParts(new RegisterParts(), new String[] { References.FRAME_PART_NAME });
    }

    @Override
    public TMultiPart createPart(String type, boolean client) {

        if (type == References.FRAME_PART_NAME)
            return new PartFrame();

        return null;
    }

}
