package com.amadornes.framez.api.movement;

public interface IMovementHandler {

    public boolean startMoving(Object obj);

    public boolean finishMoving(Object obj);

    public boolean canMove(Object obj);

    public boolean canHandle(Object o);

}
