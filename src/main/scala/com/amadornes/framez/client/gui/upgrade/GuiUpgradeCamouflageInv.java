package com.amadornes.framez.client.gui.upgrade;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketUpgradeCamoUpdate;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage.MotorUpgradeCamouflageData;

public class GuiUpgradeCamouflageInv extends GuiScreen {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/upgrade_camo_inv.png");

    private IMotor motor;
    private int id;
    private int side;
    private GuiScreen parent;

    private int xSize, ySize;

    private RenderItem itemRender = new RenderItem();

    public GuiUpgradeCamouflageInv(IMotor motor, int id, int side, GuiScreen parent) {

        this.motor = motor;
        this.id = id;
        this.side = side;
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

        MotorUpgradeCamouflageData data = (MotorUpgradeCamouflageData) motor.getUpgrades()[id].getData();

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;
        EntityPlayer player = mc.thePlayer;

        mc.renderEngine.bindTexture(resLoc);
        drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        if (data.getCamo(side) != null) {
            drawTexturedModelRectFromIcon(x_ + 76, y_ + 31, ((ItemBlock) data.getCamo(side).getItem()).field_150939_a.getIcon(
                    data.getCamoFace(side), data.getCamo(side).getItemDamage()), 24, 24);

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), data.getCamo(side), x_ + 8, y_ + 8);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < 36; i++) {
                int ix = x_ + 8 + (18 * (i % 9));
                int iy = y_ + 84 + (18 * (i / 9)) + (i >= 27 ? 4 : 0);

                int s = (i + 9) % 36;
                ItemStack is = player.inventory.getStackInSlot(s);

                if (is != null) {
                    itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy);
                    itemRender.renderItemOverlayIntoGUI(fontRendererObj, this.mc.getTextureManager(), is, ix, iy, is.stackSize <= 1 ? ""
                            : is.stackSize + "");

                }
            }
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int b) {

        MotorUpgradeCamouflageData data = (MotorUpgradeCamouflageData) motor.getUpgrades()[id].getData();

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

            if (is == null) {
                NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, side, -2, 0));
            }
            if (is != null && !(is.getItem() instanceof ItemBlock))
                return;

            NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, side, s, data.getCamoFace(side)));
        }
    }

    @Override
    protected void actionPerformed(GuiButton btn) {

        super.actionPerformed(btn);
    }

    @Override
    protected void keyTyped(char key, int keycode) {

        if (keycode == 1)
            mc.displayGuiScreen(parent);
    }

}
