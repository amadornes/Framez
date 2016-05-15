package com.amadornes.framez.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;

public abstract class SubGuiContainer extends GuiContainer {

    protected final GuiScreen parent;

    public SubGuiContainer(GuiScreen parent, Container container) {

        super(container);
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (parent != null) {
            parent.drawScreen(mouseX, mouseY, partialTicks);
            GlStateManager.disableLighting();
            GlStateManager.color(1, 1, 1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawDefaultBackground() {

        if (mc.currentScreen == this) super.drawDefaultBackground();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (keyCode == 1 || keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            int mx = Mouse.getX(), my = Mouse.getY();
            mc.thePlayer.closeScreen();
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                mc.displayGuiScreen(parent);
                if (parent != null) Mouse.setCursorPosition(mx, my);
                if (mc.currentScreen == null) mc.setIngameFocus();
            }
            return;
        }

        this.checkHotbarKeys(keyCode);

        // if (this.theSlot != null && this.theSlot.getHasStack()) {
        // if (keyCode == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
        // this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, ClickType.SWAP);// TODO: Check this. It's probably broken!
        // } else if (keyCode == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
        // this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : ClickType.PICKUP, ClickType.CLONE);
        // }
        // }
    }

}
