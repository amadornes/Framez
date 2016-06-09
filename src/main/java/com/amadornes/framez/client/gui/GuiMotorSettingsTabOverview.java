package com.amadornes.framez.client.gui;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.FramezUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiMotorSettingsTabOverview extends GuiMotorSettingsTab {

    public GuiMotorSettingsTabOverview(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        super(motor, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        drawString(fontRendererObj, I18n.format("gui.framez:motor.overview") + ":", left + 8, top + 8, 0xFFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.translate(left + 12, top + 20, 0);
        GlStateManager.scale(0.75, 0.75, 1.0);
        motor.get().gatherVariables().entrySet().stream().filter(e -> e.getKey().shouldDisplayInOverview()).limit(11).reduce(0, (a, b) -> {
            drawString(fontRendererObj,
                    I18n.format(b.getKey().getUnlocalizedName()) + ": " + FramezUtils.valueToString(b.getKey(), b.getValue()), 0, 10 * a,
                    0xBBBBBB);
            return a + 1;
        }, (a, b) -> b);
        GlStateManager.popMatrix();
    }

}
