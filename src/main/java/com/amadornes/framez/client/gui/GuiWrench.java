package com.amadornes.framez.client.gui;

import java.io.IOException;

import com.amadornes.framez.init.FramezConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class GuiWrench extends GuiScreen implements IFramezGUI {

    private final int s1 = 0x60101010, s2 = 0x70101010;
    private final int h1 = 0x80424D62, h2 = 0x90424D62;
    private final int o1 = 0x8042624D, o2 = 0x9042624D;
    private final int ho1 = 0x80426262, ho2 = 0x90426262;

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int x = width / 2;
        int y = height / 2;
        int bw = 125;
        int bh = 20;
        int m = 3;
        boolean hover = false;

        drawGradientRect(x - 3 * m - bw, y - 6 * m - bh, x + 3 * m + bw, y + 3 * m + bh, 0x60101010, 0x70101010);
        drawCenteredString(fontRendererObj, I18n.format("gui.framez:csettings.title"), x, y - m * 5 - bh + 1, 0xFFFFFF);

        // Top Left
        hover = isWithinBounds(x - m - bw, y - m - bh, bw, bh, mouseX, mouseY);
        drawGradientRect(x - m - bw, y - m - bh, x - m, y - m,
                hover ? (FramezConfig.Client.connectContiguousFrames ? ho1 : h1) : (FramezConfig.Client.connectContiguousFrames ? o1 : s1),
                hover ? (FramezConfig.Client.connectContiguousFrames ? ho2 : h2) : (FramezConfig.Client.connectContiguousFrames ? o2 : s2));
        drawCenteredString(fontRendererObj, I18n.format("gui.framez:csettings.1"), x - m - bw / 2,
                y - m - (bh + fontRendererObj.FONT_HEIGHT - 1) / 2, 0xEEEEEE);

        // Top Right
        hover = isWithinBounds(x + m, y - m - bh, bw, bh, mouseX, mouseY);
        drawGradientRect(x + m, y - m - bh, x + m + bw, y - m,
                hover ? (FramezConfig.Client.clickThroughFrames ? ho1 : h1) : (FramezConfig.Client.clickThroughFrames ? o1 : s1),
                hover ? (FramezConfig.Client.clickThroughFrames ? ho2 : h2) : (FramezConfig.Client.clickThroughFrames ? o2 : s2));
        drawCenteredString(fontRendererObj, I18n.format("gui.framez:csettings.2"), x + m + bw / 2,
                y - m - (bh + fontRendererObj.FONT_HEIGHT - 1) / 2, 0xEEEEEE);

        // Bottom Left
        hover = false;// isWithinBounds(x - m - bw, y + m, bw, bh, mouseX, mouseY);
        drawGradientRect(x - m - bw, y + m, x - m, y + m + bh, hover ? h1 : s1, hover ? h2 : s2);
        drawCenteredString(fontRendererObj, I18n.format("gui.framez:csettings.3"), x - m - bw / 2,
                y + m + (bh - fontRendererObj.FONT_HEIGHT + 1) / 2, 0xEEEEEE);

        // Bottom Right
        hover = false;// isWithinBounds(x + m, y + m, bw, bh, mouseX, mouseY);
        drawGradientRect(x + m, y + m, x + m + bw, y + m + bh, hover ? h1 : s1, hover ? h2 : s2);
        drawCenteredString(fontRendererObj, I18n.format("gui.framez:csettings.4"), x + m + bw / 2,
                y + m + (bh - fontRendererObj.FONT_HEIGHT + 1) / 2, 0xEEEEEE);
    }

    @SuppressWarnings("unused")
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        int x = width / 2;
        int y = height / 2;
        int bw = 125;
        int bh = 20;
        int m = 3;
        boolean reRender = false;

        // Top Left
        if (isWithinBounds(x - m - bw, y - m - bh, bw, bh, mouseX, mouseY)) {
            FramezConfig.Client.connectContiguousFrames = !FramezConfig.Client.connectContiguousFrames;
            reRender = true;
        }

        // Top Right
        if (isWithinBounds(x + m, y - m - bh, bw, bh, mouseX, mouseY)) {
            FramezConfig.Client.clickThroughFrames = !FramezConfig.Client.clickThroughFrames;
            reRender = true;
        }

        // Bottom Left
        if (false) {// isWithinBounds(x - m - bw, y + m, bw, bh, mouseX, mouseY)

        }

        // Bottom Right
        if (false) {// isWithinBounds(x + m, y + m, bw, bh, mouseX, mouseY);

        }

        if (reRender) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            BlockPos ppos = player.getPosition();
            Vec3i dist = new Vec3i(16 * 8, 16 * 8, 16 * 8);
            player.worldObj.markBlockRangeForRenderUpdate(ppos.subtract(dist), ppos.add(dist));
        }
    }

}
