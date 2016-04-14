package com.amadornes.framez.client.render;

import com.amadornes.framez.Framez;
import com.amadornes.framez.motor.logic.IMotorLogic;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.client.model.animation.FastTESR;

public class FTESRMotor extends FastTESR<TileMotor> {

    public static final RenderMotor<?>[] FTESRS;
    public static final boolean ALWAYS_RENDER;
    static {
        FTESRS = new RenderMotor[IMotorLogic.TYPE_FTESRS.length];
        boolean always = false;
        for (int i = 0; i < FTESRS.length; i++) {
            String s = IMotorLogic.TYPE_FTESRS[i];
            if (s != null) {
                try {
                    FTESRS[i] = (RenderMotor<?>) Class.forName(Framez.class.getPackage().getName() + ".client.render." + s).newInstance();
                    always |= FTESRS[i].alwaysRender();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ALWAYS_RENDER = always;
    }

    @Override
    public void renderTileEntityFast(TileMotor te, double x, double y, double z, float partialTicks, int destroyStage,
            WorldRenderer worldRenderer) {

        IMotorLogic logic = te.getLogic();
        RenderMotor<?> renderer = FTESRS[logic.getID()];
        if (renderer != null) render(renderer, logic, x, y, z, partialTicks, destroyStage, worldRenderer);
    }

    @SuppressWarnings("unchecked")
    private <T extends IMotorLogic> void render(RenderMotor<T> renderer, IMotorLogic logic, double x, double y, double z,
            float partialTicks, int destroyStage, WorldRenderer wr) {

        renderer.renderMotor((T) logic, x, y, z, partialTicks, destroyStage, wr);
    }

    @Override
    public boolean func_181055_a() {

        return ALWAYS_RENDER;
    }

}
