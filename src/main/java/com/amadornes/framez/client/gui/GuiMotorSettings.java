package com.amadornes.framez.client.gui;

import java.io.IOException;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiMotorSettings extends GuiScreen {

    private final int xSize = 226, ySize = 156;

    private final GuiMotorSettingsTab[] tabs;
    private int tab = 0;

    @SuppressWarnings("unused")
    private final DynamicReference<TileMotor> motor;

    public GuiMotorSettings(DynamicReference<TileMotor> motor) {

        this.tabs = new GuiMotorSettingsTab[] { //
                new GuiMotorSettingsTabOverview(motor, xSize, ySize), //
                new GuiMotorSettingsTabUpgrades(motor, xSize, ySize), //
                new GuiMotorSettingsTabTriggers(motor, xSize, ySize), //
                new GuiMotorSettingsTabSpeed(motor, xSize, ySize), //
                new GuiMotorSettingsTabIssues(motor, xSize, ySize)//
        };

        this.motor = motor;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (Minecraft.getMinecraft().currentScreen == this) this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_base.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        for (int i = 0; i < tabs.length; i++) {
            boolean selected = tab == i;
            drawTexturedModalRect(left + xSize - (selected ? 3 : 0), top + 7 + 29 * i, xSize + (selected ? 0 : 3), 7 + 29 * i,
                    26 + (selected ? 3 : 0), 26);
        }

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

        for (int i = 0; i < tabs.length; i++) {
            if (tab == i) continue;
            int x = left + xSize, y = top + 7 + 29 * i;
            int w = 26, h = 26;
            if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h) {
                tab = i;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return;
            }
        }

        tabs[tab].mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        tabs[tab].mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

        tabs[tab].mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {

        super.setWorldAndResolution(mc, width, height);
        for (int i = 0; i < tabs.length; i++)
            tabs[i].setWorldAndResolution(mc, width, height);
    }

}
