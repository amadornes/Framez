package com.amadornes.framez.client.gui;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.container.ContainerUpgrade;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiUpgradeSelect extends SubGuiContainer {

    public GuiUpgradeSelect(DynamicReference<TileMotor> motor, int upgradeSlotID, IInventory playerInventory) {

        super(Minecraft.getMinecraft().currentScreen, new ContainerUpgrade(motor, upgradeSlotID, playerInventory));

        this.xSize = 176;
        this.ySize = 168;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/gui/motor_upgrade.png"));
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }

}
