package com.amadornes.framez.api;

public final class DynamicReference<T> {

    private T value;

    public DynamicReference(T value) {
        this.value = value;
    }

    public T get() {

        return value;
    }

    public void set(T value) {

        this.value = value;
    }

}
