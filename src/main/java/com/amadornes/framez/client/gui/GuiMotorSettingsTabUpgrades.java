package com.amadornes.framez.client.gui;

import java.io.IOException;
import java.util.Map.Entry;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.PacketShowGUI;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiMotorSettingsTabUpgrades extends GuiMotorSettingsTab {

    public GuiMotorSettingsTabUpgrades(DynamicReference<TileMotor> motor, int xSize, int ySize) {

        super(motor, xSize, ySize);
    }

    @Override
    public void initGui() {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        GuiButton10 b;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                buttonList.add(b = new GuiButton10(buttonList.size(), left + 67 + x * 37, top + 54 + y * 36, 18, "..."));
                b.enabled = false;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        drawString(fontRendererObj, I18n.format("gui.framez:motor.upgrades") + ":", left + 8, top + 8, 0xFFFFFF);

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_tab_upgrades.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int sX = left + 68 + x * 37;
                int sY = top + 36 + y * 36;
                Entry<IMotorUpgrade, ItemStack> e = motor.get().getUpgrade(x * 3 + y);
                if (e != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemIntoGUI(e.getValue(), sX, sY);
                    RenderHelper.disableStandardItemLighting();
                }
                if (mouseX >= sX && mouseX < sX + 16 && mouseY >= sY && mouseY < sY + 16) {
                    GlStateManager.colorMask(true, true, true, false);
                    drawRect(sX, sY, sX + 16, sY + 16, 0x80FFFFFF);
                    GlStateManager.colorMask(true, true, true, true);
                }
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();

        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int i = 0; i < buttonList.size(); i++) {
            Entry<IMotorUpgrade, ItemStack> upgrade = motor.get().getUpgrade(i);
            buttonList.get(i).enabled = upgrade != null && upgrade.getKey().hasConfigGUI();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        TileMotor motor = this.motor.get();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                int sX = left + 68 + x * 37;
                int sY = top + 36 + y * 36;
                if (mouseX >= sX && mouseX < sX + 16 && mouseY >= sY && mouseY < sY + 16) {
                    NetworkHandler.instance.sendToServer(new PacketShowGUI(x * 3 + y, motor.getPos()));
                    return;
                }
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {

        Entry<IMotorUpgrade, ItemStack> upgrade = motor.get().getUpgrade(button.id);
        if (upgrade != null) {
            GuiScreen gui = upgrade.getKey().getConfigGUI(mc.thePlayer, this);
            if (gui instanceof GuiContainer) NetworkHandler.instance.sendToServer(new PacketShowGUI(button.id + 9, motor.get().getPos()));
            else mc.displayGuiScreen(gui);
        }
    }

}
