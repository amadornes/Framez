package com.amadornes.framez.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketUpgradeMotor;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.tile.TileMotor;

public class GuiUpgrade extends GuiScreen {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/upgrade.png");

    private TileMotor motor;
    private int slot;

    private GuiMotorSettings parent;

    private int xSize, ySize;

    private RenderItem itemRender = new RenderItem();

    public GuiUpgrade(TileMotor motor, int slot, GuiMotorSettings parent) {

        this.motor = motor;
        this.slot = slot;

        this.parent = parent;

        xSize = 176;
        ySize = 166;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;
        EntityPlayer player = mc.thePlayer;

        mc.renderEngine.bindTexture(resLoc);
        drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < 36; i++) {
                int ix = x_ + 8 + (18 * (i % 9));
                int iy = y_ + 84 + (18 * (i / 9)) + (i >= 27 ? 4 : 0);

                int s = (i + 9) % 36;
                ItemStack is = player.inventory.getStackInSlot(s);
                boolean isValidUpgrade = false;

                if (is != null) {
                    itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy);
                    itemRender.renderItemOverlayIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy, is.stackSize <= 1 ? ""
                            : is.stackSize + "");

                    for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                        if (u.isUpgradeStack(is) && u.canApply(motor, is, player)) {
                            isValidUpgrade = true;
                            break;
                        }
                    }
                }
                if (!isValidUpgrade) {
                    RenderHelper.enableStandardItemLighting();
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    GL11.glColor4d(1, 1, 1, 0.8);

                    this.zLevel = 250.0F;
                    mc.renderEngine.bindTexture(resLoc);
                    drawTexturedModalRect(ix - 1, iy - 1, ix - x_ - 1, iy - y_ - 1, 18, 18);

                    GL11.glColor4d(1, 1, 1, 1);

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    RenderHelper.disableStandardItemLighting();
                    RenderHelper.enableGUIStandardItemLighting();

                    this.zLevel = 0.0F;
                    itemRender.zLevel = 0.0F;
                }
            }
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            for (int i = 0; i < 36; i++) {
                int ix = x_ + 8 + (18 * (i % 9));
                int iy = y_ + 84 + (18 * (i / 9)) + (i >= 27 ? 4 : 0);

                if (!(x > ix && x <= ix + 16 && y > iy && y <= iy + 16))
                    continue;

                int s = (i + 9) % 36;
                ItemStack is = player.inventory.getStackInSlot(s);
                IMotorUpgrade upgrade = null;

                if (is != null) {
                    for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                        if (u.isUpgradeStack(is) && u.canApply(motor, is, player)) {
                            upgrade = u;
                            break;
                        }
                    }
                }
                if (upgrade != null)
                    GuiHelper.drawHoveringText(
                            Arrays.asList(is.getDisplayName(), EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString()
                                    + I18n.format("upgrade.framez:" + upgrade.getType()), EnumChatFormatting.YELLOW
                                    + "Click to select this upgrade."), ix + 10, iy + 5, fontRendererObj);
            }
        }

        {
            IMotorUpgradeData data = motor.getUpgrades()[slot];
            if (data == null)
                return;
            ItemStack is = data.getStack();

            int ix = x_ + 80;
            int iy = y_ + 35;

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            IMotorUpgrade upgrade = null;

            if (is != null) {
                for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                    if (u.isUpgradeStack(is) && u.canApply(motor, is, player)) {
                        upgrade = u;
                        break;
                    }
                }
            }

            ix--;
            iy--;

            if (upgrade != null && x > ix && x <= ix + 16 && y > iy && y <= iy + 16)
                GuiHelper.drawHoveringText(
                        Arrays.asList(
                                is.getDisplayName(),
                                EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString()
                                        + I18n.format("upgrade.framez:" + upgrade.getType()), EnumChatFormatting.YELLOW + "Click to "
                                        + EnumChatFormatting.RED + "remove " + EnumChatFormatting.YELLOW + "this upgrade."), ix + 10,
                        iy + 5, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int b) {

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;
        EntityPlayer player = mc.thePlayer;

        for (int i = 0; i < 36; i++) {
            int ix = x_ + 8 + (18 * (i % 9));
            int iy = y_ + 84 + (18 * (i / 9)) + (i >= 27 ? 4 : 0);

            if (!(x > ix && x <= ix + 16 && y > iy && y <= iy + 16))
                continue;

            int s = (i + 9) % 36;
            ItemStack is = player.inventory.getStackInSlot(s);
            IMotorUpgrade upgrade = null;

            if (is != null) {
                for (IMotorUpgrade u : ModifierRegistry.instance.motorUpgrades) {
                    if (u.isUpgradeStack(is) && u.canApply(motor, is, player)) {
                        upgrade = u;
                        break;
                    }
                }
            }

            if (upgrade != null)
                NetworkHandler.instance().sendToServer(new PacketUpgradeMotor(motor, slot, s));
            return;
        }

        IMotorUpgradeData data = motor.getUpgrades()[slot];
        if (data == null)
            return;
        int ix = x_ + 79;
        int iy = y_ + 34;

        if (x > ix && x <= ix + 16 && y > iy && y <= iy + 16) {
            NetworkHandler.instance().sendToServer(new PacketUpgradeMotor(motor, slot, -1));
            return;
        }
    }

    @Override
    protected void keyTyped(char key, int keycode) {

        if (keycode == 1)
            mc.displayGuiScreen(parent);
    }

}
