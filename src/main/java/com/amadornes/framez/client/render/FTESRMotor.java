package com.amadornes.framez.client.render;

import com.amadornes.framez.Framez;
import com.amadornes.framez.motor.logic.IMotorLogic;
import com.amadornes.framez.motor.logic.MotorLogicType;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.client.model.animation.FastTESR;

public class FTESRMotor extends FastTESR<TileMotor> {

    public static final RenderMotor<?>[] FTESRS;
    static {
        FTESRS = new RenderMotor[MotorLogicType.VALUES.length];
        for (int i = 0; i < FTESRS.length; i++) {
            String s = MotorLogicType.VALUES[i].ftesr;
            if (s != null) {
                try {
                    FTESRS[i] = (RenderMotor<?>) Class.forName(Framez.class.getPackage().getName() + ".client.render." + s).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void renderTileEntityFast(TileMotor te, double x, double y, double z, float partialTicks, int destroyStage,
            VertexBuffer buffer) {

        IMotorLogic<?> logic = te.getLogic();
        RenderMotor<?> renderer = FTESRS[logic.getID()];
        if (renderer != null) render(renderer, logic, x, y, z, partialTicks, destroyStage, buffer);
    }

    @SuppressWarnings("unchecked")
    private <T extends IMotorLogic<?>> void render(RenderMotor<T> renderer, IMotorLogic<?> logic, double x, double y, double z,
            float partialTicks, int destroyStage, VertexBuffer buffer) {

        renderer.renderMotor((T) logic, x, y, z, partialTicks, destroyStage, buffer);
    }

    @Override
    public boolean isGlobalRenderer(TileMotor te) {

        return isGlobal(te);
    }

    @SuppressWarnings("unchecked")
    public <T extends IMotorLogic<?>> boolean isGlobal(TileMotor te) {

        T logic = (T) te.getLogic();
        RenderMotor<T> renderer = (RenderMotor<T>) FTESRS[logic.getID()];
        if (renderer != null) return renderer.alwaysRender(logic);
        return false;
    }

}
