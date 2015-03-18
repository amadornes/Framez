package com.amadornes.framez.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import uk.co.qmunity.lib.client.gui.GuiBase;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.amadornes.framez.api.movement.MotorSetting;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

public class GuiMotorSettings extends GuiBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/settings.png");

    private TileMotor motor;

    public GuiMotorSettings(TileMotor motor) {

        super(resLoc, 228, 120, "Motor Settings");
        this.motor = motor;
    }

    @Override
    public void initGui() {

        super.initGui();

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        WidgetMode redstone_pulse = new WidgetMode(0, x_ + 8, y_ + 20, xSize + 14, 0, 2, ModInfo.MODID + ":textures/gui/settings.png");
        redstone_pulse.value = motor.getSettings().contains(MotorSetting.REDSTONE_PULSE) ? 1 : 0;
        addWidget(redstone_pulse);

        WidgetMode inverted = new WidgetMode(1, x_ + 8, y_ + 36, xSize + 14, 28, 2, ModInfo.MODID + ":textures/gui/settings.png");
        inverted.value = motor.getSettings().contains(MotorSetting.REDSTONE_INVERTED) ? 1 : 0;
        addWidget(inverted);
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        drawString(fontRendererObj, I18n.format("gui." + ModInfo.MODID + ":motor.redstone_pulse." + ((WidgetMode) getWidget(0)).value),
                x_ + 8 + 16, y_ + 20 + 3, COLOR_TEXT);
        drawString(fontRendererObj, I18n.format("gui." + ModInfo.MODID + ":motor.redstone_inverted." + ((WidgetMode) getWidget(1)).value),
                x_ + 8 + 16, y_ + 36 + 3, COLOR_TEXT);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        super.actionPerformed(widget);

        if (widget.getID() == 0)
            motor.configure(MotorSetting.REDSTONE_PULSE);
        if (widget.getID() == 1)
            motor.configure(MotorSetting.REDSTONE_INVERTED);
    }

}
