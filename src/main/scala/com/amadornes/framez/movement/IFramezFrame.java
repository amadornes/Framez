package com.amadornes.framez.movement;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IModifiableFrame;

public interface IFramezFrame extends IModifiableFrame {

    public void init(IFrameMaterial material);

}
