package com.amadornes.framez.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.ref.ModInfo;

public class GuiSettings extends GuiScreen {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/settings.png");

    private int xSize, ySize;

    public GuiSettings() {

        xSize = 228;
        ySize = 123;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {

        super.initGui();

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        GuiButton b1 = new GuiButton16(0, x_ + 8, y_ + 8);
        b1.visible = false;
        buttonList.add(b1);
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        mc.renderEngine.bindTexture(resLoc);
        drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

        super.drawScreen(x, y, partialTick);

        mc.renderEngine.bindTexture(resLoc);

        drawTexturedModalRect(x_ + 8 + 1, y_ + 8 + 1, 228, FramezConfig.click_through_frames ? 14 : 0, 14, 14);

        drawString(fontRendererObj, I18n.format("gui." + ModInfo.MODID + ":click_through_frames"), x_ + 8 + 20, y_ + 8 + 3, 0xFFFFFF);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        super.actionPerformed(button);

        if (button.id == 0)
            FramezConfig.click_through_frames = !FramezConfig.click_through_frames;

        if (button.id == 0) {
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
