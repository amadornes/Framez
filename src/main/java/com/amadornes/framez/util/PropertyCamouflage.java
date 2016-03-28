package com.amadornes.framez.util;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyCamouflage implements IUnlistedProperty<IBlockState> {

    private final String name;

    public PropertyCamouflage(String name) {

        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public boolean isValid(IBlockState value) {

        return true;
    }

    @Override
    public Class<IBlockState> getType() {

        return IBlockState.class;
    }

    @Override
    public String valueToString(IBlockState value) {

        return value.toString();
    }

}
