package com.amadornes.framez.client.gui;

import java.io.IOException;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiMotorSettingsTab extends GuiScreen {

    protected final DynamicReference<TileMotor> motor;
    protected final int xSize, ySize;

    public GuiMotorSettingsTab(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        this.motor = motor;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void drawBackground(int tint) {

    }

    @Override
    public void drawDefaultBackground() {

    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
    }

}
