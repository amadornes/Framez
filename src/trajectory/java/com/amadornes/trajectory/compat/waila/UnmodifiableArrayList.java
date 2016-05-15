package com.amadornes.trajectory.compat.waila;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnmodifiableArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    public UnmodifiableArrayList(List<T> l) {

        super(l);
    }

    @Override
    public void add(int arg0, T arg1) {

    }

    @Override
    public boolean add(T e) {

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {

        return true;
    }

    @Override
    public T remove(int index) {

        return null;
    }

    @Override
    public boolean remove(Object o) {

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        return true;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {

    }

    @Override
    public void clear() {

    }

}
