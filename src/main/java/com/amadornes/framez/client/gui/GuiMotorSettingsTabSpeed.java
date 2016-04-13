package com.amadornes.framez.client.gui;

import java.io.IOException;
import java.text.DecimalFormat;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiMotorSettingsTabSpeed extends GuiMotorSettingsTab {

    private final DecimalFormat format = new DecimalFormat("#######0.000");

    private GuiButton button;
    private boolean dragging = false;
    private int xOff = 0;

    public GuiMotorSettingsTabSpeed(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        super(motor, xSize, ySize);
    }

    @Override
    public void initGui() {

        super.initGui();

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        int sliderX = (int) (left + 13 + ((xSize - 34) * motor.get().getVariable(TileMotor.MOVEMENT_TIME) / 60D));
        buttonList.add(button = new GuiButton16(0, sliderX, top + 21, 8, ""));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_settings_speed.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        drawString(fontRendererObj, I18n.format("gui.framez:motor.speed") + ":", left + 8, top + 8, 0xFFFFFFFF);

        drawString(fontRendererObj, "Energy Applied:", left + 8, top + 87, 0xFFFFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.translate(left + 42, top + 45.5, 0);
        GlStateManager.scale(0.75, 0.75, 1.0);
        drawCenteredString(fontRendererObj, format.format(motor.get().getVariable(TileMotor.MOVEMENT_TIME)) + "s", 0, 0, 0xFFFFFFFF);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        if (dragging) GlStateManager
                .translate((Math.min(Math.max(left + 13, mouseX - xOff), left + xSize - 21) - button.xPosition) * partialTicks, 0, 0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= button.xPosition && mouseX < button.xPosition + button.width && mouseY >= button.yPosition
                && mouseY < button.yPosition + button.height) {
            dragging = true;
            xOff = mouseX - button.xPosition;
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        int left = (width - xSize) / 2;

        if (dragging) {
            button.xPosition = Math.min(Math.max(left + 13, mouseX - xOff), left + xSize - 21);
            double movementTime = ((button.xPosition - (left + 13D)) / (xSize - 34)) * 60D;
            movementTime = Math.round(movementTime * 1000D) / 1000D;
            motor.get().nativeVariables.put(TileMotor.MOVEMENT_TIME, movementTime);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) dragging = false;
    }

}
