package com.amadornes.framez.util;

import com.amadornes.framez.api.frame.IFrameMaterial;

import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyMaterial implements IUnlistedProperty<IFrameMaterial> {

    private final String name;

    public PropertyMaterial(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isValid(IFrameMaterial value) {

        return true;
    }

    @Override
    public Class<IFrameMaterial> getType() {

        return IFrameMaterial.class;
    }

    @Override
    public String valueToString(IFrameMaterial value) {

        return value.toString();
    }

}
