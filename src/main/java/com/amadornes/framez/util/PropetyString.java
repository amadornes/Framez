package com.amadornes.framez.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

import net.minecraft.block.properties.IProperty;

public class PropetyString implements IProperty<String> {

    private final String name;
    private final List<String> values;

    public PropetyString(String name, String... values) {

        this.name = name;
        this.values = Arrays.asList(values);
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Collection<String> getAllowedValues() {

        return values;
    }

    @Override
    public Class<String> getValueClass() {

        return String.class;
    }

    @Override
    public String getName(String value) {

        return value.toLowerCase();
    }

    @Override
    public Optional<String> parseValue(String value) {

        return Optional.of(value);
    }

}
