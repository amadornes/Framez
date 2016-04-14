package com.amadornes.framez.client.render;

import com.amadornes.framez.motor.logic.IMotorLogic;

import net.minecraft.client.renderer.WorldRenderer;

public abstract class RenderMotor<L extends IMotorLogic> {

    public abstract void renderMotor(L logic, double x, double y, double z, float partialTicks, int destroyStage,
            WorldRenderer wr);

    public boolean alwaysRender() {

        return false;
    }

}
