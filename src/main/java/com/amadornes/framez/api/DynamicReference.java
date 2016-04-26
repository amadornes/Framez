package com.amadornes.framez.api;

import com.google.common.base.Supplier;

public final class DynamicReference<T> implements Supplier<T> {

    private T value;

    public DynamicReference(T value) {
        this.value = value;
    }

    @Override
    public T get() {

        return value;
    }

    public void set(T value) {

        this.value = value;
    }

}
