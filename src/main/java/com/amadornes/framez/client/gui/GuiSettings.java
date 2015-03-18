package com.amadornes.framez.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.qmunity.lib.client.gui.GuiBase;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;

import com.amadornes.framez.config.Config;
import com.amadornes.framez.ref.ModInfo;

public class GuiSettings extends GuiBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/settings.png");

    public GuiSettings() {

        super(resLoc, 228, 120, "Settings");
    }

    @Override
    public void initGui() {

        super.initGui();

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        WidgetMode click_through_frames = new WidgetMode(0, x_ + 8, y_ + 20, xSize, 2, ModInfo.MODID + ":textures/gui/settings.png");
        click_through_frames.value = Config.click_through_frames ? 1 : 0;
        addWidget(click_through_frames);

        WidgetMode simple_frames = new WidgetMode(1, x_ + 8, y_ + 36, xSize, 2, ModInfo.MODID + ":textures/gui/settings.png");
        simple_frames.value = Config.simple_frames ? 1 : 0;
        addWidget(simple_frames);
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

        drawString(fontRendererObj, I18n.format("gui." + ModInfo.MODID + ":click_through_frames"), x_ + 8 + 16, y_ + 20 + 3, COLOR_TEXT);
        drawString(fontRendererObj, I18n.format("gui." + ModInfo.MODID + ":simple_frames"), x_ + 8 + 16, y_ + 36 + 3, COLOR_TEXT);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        super.actionPerformed(widget);

        if (widget.getID() == 0)
            Config.click_through_frames = ((WidgetMode) widget).value != 0;
        if (widget.getID() == 1)
            Config.simple_frames = ((WidgetMode) widget).value != 0;

        if (widget.getID() == 0 || widget.getID() == 1) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.thePlayer == null || mc.theWorld == null)
                return;

            EntityPlayer player = mc.thePlayer;

            if (player == null)
                return;

            int x = (int) player.posX;
            int y = (int) player.posY;
            int z = (int) player.posZ;

            int range = 16 * 16;

            mc.theWorld.markBlockRangeForRenderUpdate(x - range, y - range, z - range, x + range, y + range, z + range);
        }
    }

}
