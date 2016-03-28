package com.amadornes.framez.client.gui;

import java.io.IOException;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiMotorSettings extends GuiScreen {

    private final int xSize = 200, ySize = 123;

    private final GuiMotorSettingsTab[] tabs;
    private int tab = 0;

    @SuppressWarnings("unused")
    private final DynamicReference<TileMotor> motor;

    public GuiMotorSettings(DynamicReference<TileMotor> motor) {

        this.tabs = new GuiMotorSettingsTab[] { //
                new GuiMotorSettingsTabOverview(motor, xSize, ySize), //
                new GuiMotorSettingsTabUpgrades(motor, xSize, ySize), //
                new GuiMotorSettingsTabTriggers(motor, xSize, ySize), //
                new GuiMotorSettingsTabSpeed(motor, xSize, ySize)//
        };

        this.motor = motor;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_settings.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        for (int i = 0; i < 4; i++)
            drawTexturedModalRect(left + xSize - 3, top + 5 + 29 * i, xSize + (tab == i ? 24 : 0), 27 * i, 24, 27);

        tabs[tab].drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {

        tabs[tab].actionPerformed(button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {

        super.keyTyped(typedChar, keyCode);
        if (this.mc.currentScreen != null) tabs[tab].keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        if (mouseX >= left && mouseX < left + xSize && mouseY >= top && mouseY < top + ySize) {
            tabs[tab].mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        for (int i = 0; i < 4; i++) {
            int x = left + xSize, y = top + 5 + 29 * i;
            if (mouseX >= x && mouseX < x + 24 && mouseY >= y && mouseY < y + 27) {
                tab = i;
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                return;
            }
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        if (mouseX >= left && mouseX < left + xSize && mouseY >= top && mouseY < top + ySize) {
            tabs[tab].mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            return;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        if (mouseX >= left && mouseX < left + xSize && mouseY >= top && mouseY < top + ySize) {
            tabs[tab].mouseReleased(mouseX, mouseY, state);
            return;
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {

        super.setWorldAndResolution(mc, width, height);
        for (int i = 0; i < 4; i++)
            tabs[i].setWorldAndResolution(mc, width, height);
    }

}
