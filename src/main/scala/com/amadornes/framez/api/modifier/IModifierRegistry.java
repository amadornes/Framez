package com.amadornes.framez.api.modifier;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.wrench.IFramePartHandler;

public interface IModifierRegistry {

    public void registerFrameSideModifier(IFrameSideModifier modifier);

    public void registerFrameMaterial(IFrameMaterial material);

    public void registerFramePartHandler(IFramePartHandler handler);

    public void registerMotorModifier(IMotorModifier modifier);

    public void registerMotorUpgrade(IMotorUpgrade upgrade);

}
