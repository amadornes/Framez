package com.amadornes.framez.api.movement;

public interface IMovementApi {

    public void registerMovementListener(IMovementListener listener);

    public IMovementListener[] getListeners(Object obj);

    public IMovementListener[] getRegisteredListeners();

    public void registerMovementHandler(IMovementHandler listener);

    public IMovementHandler[] getHandlers(Object obj);

    public IMovementHandler[] getRegisteredHandlers();

}
