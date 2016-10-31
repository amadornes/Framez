package com.amadornes.framez.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public abstract class SubGuiScreen extends GuiScreen {

    protected final GuiScreen parent;

    public SubGuiScreen(GuiScreen parent) {

        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (parent != null) {
            parent.drawScreen(mouseX, mouseY, partialTicks);
            drawDefaultBackground();
            GlStateManager.disableLighting();
            GlStateManager.color(1, 1, 1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawDefaultBackground() {

        if (mc.currentScreen == this) {
            super.drawDefaultBackground();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (keyCode == 1 || keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            int mx = Mouse.getX(), my = Mouse.getY();
            mc.thePlayer.closeScreen();
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                mc.displayGuiScreen(parent);
                if (parent != null) {
                    Mouse.setCursorPosition(mx, my);
                }
                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }
            return;
        }
    }

}
