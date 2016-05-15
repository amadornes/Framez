package com.amadornes.framez.client.gui.upgrade;

import java.util.Arrays;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.client.gui.GuiHelper;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketUpgradeCamoUpdate;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage.MotorUpgradeCamouflageData;

public class GuiUpgradeCamouflage extends GuiScreen {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "textures/gui/upgrade_camo.png");

    private IMotor motor;
    private int id;
    private GuiScreen parent;

    private int xSize, ySize;

    public GuiUpgradeCamouflage(IMotor motor, int id, GuiScreen parent) {

        this.motor = motor;
        this.id = id;
        this.parent = parent;

        xSize = 228;
        ySize = 123;
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

        mc.renderEngine.bindTexture(resLoc);
        drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        if (data.getCamo(0) != null)
            drawTexturedModelRectFromIcon(x_ + 106 + 31, y_ + 20 + 31 + 31,
                    ((ItemBlock) data.getCamo(0).getItem()).field_150939_a.getIcon(data.getCamoFace(0), data.getCamo(0).getItemDamage()),
                    24, 24);
        if (data.getCamo(1) != null)
            drawTexturedModelRectFromIcon(x_ + 106, y_ + 20 + 31,
                    ((ItemBlock) data.getCamo(1).getItem()).field_150939_a.getIcon(data.getCamoFace(1), data.getCamo(1).getItemDamage()),
                    24, 24);
        if (data.getCamo(2) != null)
            drawTexturedModelRectFromIcon(x_ + 106, y_ + 20,
                    ((ItemBlock) data.getCamo(2).getItem()).field_150939_a.getIcon(data.getCamoFace(2), data.getCamo(2).getItemDamage()),
                    24, 24);
        if (data.getCamo(3) != null)
            drawTexturedModelRectFromIcon(x_ + 106, y_ + 20 + 31 + 31,
                    ((ItemBlock) data.getCamo(3).getItem()).field_150939_a.getIcon(data.getCamoFace(3), data.getCamo(3).getItemDamage()),
                    24, 24);
        if (data.getCamo(4) != null)
            drawTexturedModelRectFromIcon(x_ + 106 - 31, y_ + 20 + 31,
                    ((ItemBlock) data.getCamo(4).getItem()).field_150939_a.getIcon(data.getCamoFace(4), data.getCamo(4).getItemDamage()),
                    24, 24);
        if (data.getCamo(5) != null)
            drawTexturedModelRectFromIcon(x_ + 106 + 31, y_ + 20 + 31,
                    ((ItemBlock) data.getCamo(5).getItem()).field_150939_a.getIcon(data.getCamoFace(5), data.getCamo(5).getItemDamage()),
                    24, 24);

        if (x >= x_ + 106 && x <= x_ + 129) {
            if (y >= y_ + 21 && y <= y_ + 43) {
                GuiHelper.drawHoveringText(Arrays.asList("North"), x_ + 125, y_ + 29, fontRendererObj);
            } else if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
                GuiHelper.drawHoveringText(Arrays.asList("Top"), x_ + 125, y_ + 29 + 31, fontRendererObj);
            } else if (y >= y_ + 21 + 31 + 31 && y <= y_ + 43 + 31 + 31) {
                GuiHelper.drawHoveringText(Arrays.asList("South"), x_ + 125, y_ + 29 + 31 + 31, fontRendererObj);
            }
        } else if (x >= x_ + 106 + 31 && x <= x_ + 129 + 31) {
            if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
                GuiHelper.drawHoveringText(Arrays.asList("East"), x_ + 125 + 31, y_ + 29 + 31, fontRendererObj);
            } else if (y >= y_ + 21 + 31 + 31 && y <= y_ + 43 + 31 + 31) {
                GuiHelper.drawHoveringText(Arrays.asList("Bottom"), x_ + 125 + 31, y_ + 29 + 31 + 31, fontRendererObj);
            }
        } else if (x >= x_ + 106 - 31 && x <= x_ + 129 - 31) {
            if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
                GuiHelper.drawHoveringText(Arrays.asList("West"), x_ + 125 - 31, y_ + 29 + 31, fontRendererObj);
            }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int b) {

        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        int x_ = (width - xSize) / 2;
        int y_ = (height - ySize) / 2;

        if (x >= x_ + 106 && x <= x_ + 129) {
            if (y >= y_ + 21 && y <= y_ + 43) {
                if (shift)
                    NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 2, -2, 2));
                else
                    mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 2, this));
            } else if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
                if (shift)
                    NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 1, -2, 1));
                else
                    mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 1, this));
            } else if (y >= y_ + 21 + 31 + 31 && y <= y_ + 43 + 31 + 31) {
                if (shift)
                    NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 3, -2, 3));
                else
                    mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 3, this));
            }
        } else if (x >= x_ + 106 + 31 && x <= x_ + 129 + 31) {
            if (shift)
                NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 5, -2, 5));
            else
                mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 5, this));
            if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
            } else if (y >= y_ + 21 + 31 + 31 && y <= y_ + 43 + 31 + 31) {
                if (shift)
                    NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 0, -2, 0));
                else
                    mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 0, this));
            }
        } else if (x >= x_ + 106 - 31 && x <= x_ + 129 - 31) {
            if (y >= y_ + 21 + 31 && y <= y_ + 43 + 31) {
                if (shift)
                    NetworkHandler.instance().sendToServer(new PacketUpgradeCamoUpdate(motor, 4, -2, 4));
                else
                    mc.displayGuiScreen(new GuiUpgradeCamouflageInv(motor, id, 4, this));
            }
        }
    }

    @Override
    protected void keyTyped(char key, int keycode) {

        if (keycode == 1)
            mc.displayGuiScreen(parent);
    }

}
