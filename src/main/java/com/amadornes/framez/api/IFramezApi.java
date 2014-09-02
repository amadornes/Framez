package com.amadornes.framez.api;

import com.amadornes.framez.api.movement.IMovementApi;

public interface IFramezApi {

    public IModifierRegistry getModifierRegistry();

    public IMotorRegistry getMotorRegistry();

    public IMovementApi getMovementApi();

}
