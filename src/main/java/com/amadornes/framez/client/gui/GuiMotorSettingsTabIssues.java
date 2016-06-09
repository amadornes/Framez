package com.amadornes.framez.client.gui;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.resources.I18n;

public class GuiMotorSettingsTabIssues extends GuiMotorSettingsTab {

    public GuiMotorSettingsTabIssues(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        super(motor, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        drawString(fontRendererObj, I18n.format("gui.framez:motor.issues") + ":", left + 8, top + 8, 0xFFFFFF);
    }

}
