package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.movement.IMovementApi;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovementListener;

public class MovementApi implements IMovementApi {

    public static final MovementApi INST = new MovementApi();

    private List<IMovementListener> listeners = new ArrayList<IMovementListener>();
    private List<IMovementHandler> handlers = new ArrayList<IMovementHandler>();

    private MovementApi() {

    }

    @Override
    public void registerMovementListener(IMovementListener listener) {

        if (listener != null)
            listeners.add(listener);
    }

    @Override
    public IMovementListener[] getListeners(Object obj) {

        List<IMovementListener> l = new ArrayList<IMovementListener>();

        for (IMovementListener listener : listeners)
            if (listener.canHandle(obj))
                l.add(listener);

        IMovementListener[] arr = l.toArray(new IMovementListener[0]);
        l.clear();
        return arr;
    }

    @Override
    public IMovementListener[] getRegisteredListeners() {

        return listeners.toArray(new IMovementListener[0]);
    }

    @Override
    public void registerMovementHandler(IMovementHandler handler) {

        if (handler != null)
            handlers.add(handler);
    }

    @Override
    public IMovementHandler[] getHandlers(Object obj) {

        List<IMovementHandler> l = new ArrayList<IMovementHandler>();

        for (IMovementHandler handler : handlers)
            if (handler.canHandle(obj))
                l.add(handler);

        IMovementHandler[] arr = l.toArray(new IMovementHandler[0]);
        l.clear();
        return arr;
    }

    @Override
    public IMovementHandler[] getRegisteredHandlers() {

        return handlers.toArray(new IMovementHandler[0]);
    }

    public void onPlace(Object o, ForgeDirection d) {

        for (IMovementListener l : getListeners(o))
            l.onFinishMoving(o, d);
    }

    public void onRemove(Object o, ForgeDirection d) {

        for (IMovementListener l : getListeners(o))
            l.onStartMoving(o, d);
    }

}
