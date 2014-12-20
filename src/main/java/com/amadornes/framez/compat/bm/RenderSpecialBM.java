package com.amadornes.framez.compat.bm;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.api.IMotor;
import com.amadornes.framez.api.IRenderMotorSpecial;
import com.amadornes.framez.client.render.RenderHelper;
import com.amadornes.framez.tile.TileMotor;

public class RenderSpecialBM implements IRenderMotorSpecial {

    private int list;

    @Override
    public boolean shouldRender(TileMotor motor, ForgeDirection face) {

        return motor instanceof TileMotorBM && face == ForgeDirection.UP;
    }

    @Override
    public boolean shouldRender(ItemStack item, ForgeDirection face) {

        if (item.getItem() instanceof ItemBlock) {
            Block b = Block.getBlockFromItem(item.getItem());
            if (b instanceof IMotor && ((IMotor) b).getProvider() instanceof MotorProviderBM) {
                return face == ForgeDirection.UP;
            }
        }

        return false;
    }

    @Override
    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame) {

        TileMotorBM te = (TileMotorBM) motor;

        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;

        Entry<Double, Double> info = te.getExtraInfo();

        double powerPercentage = info.getValue() / info.getKey();

        float brightness = ((System.currentTimeMillis() % 2000) / 1000F);
        if (brightness > 1)
            brightness = 1 - (brightness - 1);
        brightness = (brightness * 0.75F * (motor.getWorldObj().getBlockPowerInput(motor.xCoord, motor.yCoord, motor.zCoord) / 16F)) + 0.25F;
        brightness *= 240F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glRotated(90, 0, 1, 0);
        GL11.glTranslated(-0.5, -0.5, -0.5);

        GL11.glTranslated(0.2, 0, 0.2);
        GL11.glScaled(0.6, 1, 0.6);

        GL11.glColor4d(1, 0, 0, 1);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);
        renderBloodOrb(false, powerPercentage);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20, 20);
        renderBloodOrb(false, -powerPercentage);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
    }

    @Override
    public void renderSpecial(ItemStack item, ForgeDirection face, float frame) {

        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glRotated(180, 0, 1, 0);
        GL11.glTranslated(-0.5, -0.5, -0.5);

        GL11.glTranslated(0.2, 0, 0.2);
        GL11.glScaled(0.6, 1, 0.6);

        GL11.glColor4d(1, 0, 0, 1);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20, 20);
        renderBloodOrb(false, 0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
    }

    public void renderBloodOrb(boolean inverted, double powerPercentage) {

        double p = (Math.abs(powerPercentage) * 1.05) - 0.025;

        GL11.glTranslated(p, 0, 0);
        if (powerPercentage > 0) {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 1, 0, 0, 0, 0, 0, 1, 1));
        } else {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 0, 0, 0, 1, 0, 0, 1, 1));
        }
        GL11.glTranslated(-p, 0, 0);

        GL11.glEnable(GL11.GL_CLIP_PLANE0);

        renderBloodOrb();

        GL11.glDisable(GL11.GL_CLIP_PLANE0);
    }

    private void renderBloodOrb() {

        if (list == 0) {
            list = GL11.glGenLists(1);
            GL11.glNewList(list, GL11.GL_COMPILE);
            renderBloodOrb_do();
            GL11.glEndList();
        }

        GL11.glCallList(list);
    }

    private void renderBloodOrb_do() {

        double depth = 1;

        GL11.glNormal3d(0, 1, 0);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        {
            RenderHelper.vertex(0.5, 1, 0.5);
            RenderHelper.vertex(10 / 16D, 1, 0);//
            RenderHelper.vertex(6 / 16D, 1, 0);//
            RenderHelper.vertex(6 / 16D, 1, 1 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 1 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 2 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 2 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(1 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(1 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(0 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(0 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(1 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(1 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 15 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 15 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(15 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(15 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(16 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(16 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(15 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(15 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 2 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 2 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 1 / 16D);//
            RenderHelper.vertex(10 / 16D, 1, 1 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 0);
        }
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 0);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 0);
            RenderHelper.vertex(6 / 16D, 1, 0);
            RenderHelper.vertex(10 / 16D, 1, 0);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 0);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 1 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 0);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 1 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 1 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 2 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 1 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 2 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 2 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 2 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 4 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 4 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 7 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 7 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(0 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(0 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 8 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(0 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(0 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(0 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(0 / 16D, 1, 8 / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(0 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(0 / 16D, 1, 12 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 12 / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(1 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(1 / 16D, 1, 13 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 13 / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 13 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 14 / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 15 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 15 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 14 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 15 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 15 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 15 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 15 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 15 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 15 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 14 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 14 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 13 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 13 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(16 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(16 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 12 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(16 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(16 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(16 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(16 / 16D, 1, 12 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(16 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(16 / 16D, 1, 8 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 8 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(15 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(15 / 16D, 1, 7 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 7 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 4 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 2 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 4 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 2 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 2 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 2 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 1 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 2 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 1 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 1 / 16D);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 1 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 0);
            RenderHelper.vertex(10 / 16D, 1, 0);
            RenderHelper.vertex(10 / 16D, 1, 1 / 16D);
        }

        GL11.glEnd();
    }
}
