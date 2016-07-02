package com.amadornes.framez.client.render;

import com.amadornes.framez.motor.logic.IMotorLogic;

import net.minecraft.client.renderer.VertexBuffer;

public abstract class RenderMotor<L extends IMotorLogic<?>> {

    public abstract void renderMotor(L logic, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buffer);

    public boolean alwaysRender(L logic) {

        return false;
    }

}
