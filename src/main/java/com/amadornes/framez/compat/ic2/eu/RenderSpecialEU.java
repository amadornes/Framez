package com.amadornes.framez.compat.ic2.eu;

import java.util.Map.Entry;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.client.render.IRenderMotorSpecial;
import com.amadornes.framez.tile.TileMotor;

public class RenderSpecialEU implements IRenderMotorSpecial {

    @Override
    public boolean shouldRender(TileMotor motor, ForgeDirection face) {

        return motor instanceof TileMotorEU && face == ForgeDirection.UP;
    }

    @Override
    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame) {

        TileMotorEU te = (TileMotorEU) motor;

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
        GL11.glRotated(180, 0, 1, 0);
        GL11.glTranslated(-0.5, -0.5, -0.5);

        GL11.glTranslated(0.25, 0, 0.25);
        GL11.glScaled(0.5, 1, 0.5);

        GL11.glColor4d(1, 0, 0, 1);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);
        renderLightningBolt(false, powerPercentage);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20, 20);
        renderLightningBolt(false, -powerPercentage);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
    }

    public void renderLightningBolt(boolean inverted, double powerPercentage) {

        double p = Math.abs(powerPercentage) * 1.001;

        GL11.glTranslated(p, 0, 0);
        if (powerPercentage > 0) {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 1, 0, 0, 0, 0, 0, 1, 1));
        } else {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 0, 0, 0, 1, 0, 0, 1, 1));
        }
        GL11.glTranslated(-p, 0, 0);

        GL11.glEnable(GL11.GL_CLIP_PLANE0);

        double depth = 1;

        GL11.glNormal3d(0, 1, 0);
        GL11.glBegin(GL11.GL_POLYGON);
        {
            RenderHelper.vertex(0.5, 1, 0.4);
            RenderHelper.vertex(0, 1, 0.1);
            RenderHelper.vertex(0.5, 1, 0.6);
            RenderHelper.vertex(0.5, 1, 0.5);
            RenderHelper.vertex(1, 1, 1);
            RenderHelper.vertex(1, 1, 0.5);
            RenderHelper.vertex(0.4, 1, 0.2);
            RenderHelper.vertex(0.4, 1, 0.4);
        }
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glNormal3d(-0.5, 0, -0.5);
        {
            RenderHelper.vertex(0, 1 - (depth / 16D), 0.1);
            RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.6);
            RenderHelper.vertex(0.5, 1, 0.6);
            RenderHelper.vertex(0, 1, 0.1);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.6);
            RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5);
            RenderHelper.vertex(0.5, 1, 0.5);
            RenderHelper.vertex(0.5, 1, 0.6);
        }

        GL11.glNormal3d(-0.5, 0, -0.5);
        {
            RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5);
            RenderHelper.vertex(1, 1 - (depth / 16D), 1);
            RenderHelper.vertex(1, 1, 1);
            RenderHelper.vertex(0.5, 1, 0.5);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(1, 1 - (depth / 16D), 1);
            RenderHelper.vertex(1, 1 - (depth / 16D), 0.5);
            RenderHelper.vertex(1, 1, 0.5);
            RenderHelper.vertex(1, 1, 1);
        }

        GL11.glNormal3d(0.5, 0, 0.5);
        {
            RenderHelper.vertex(1, 1 - (depth / 16D), 0.5);
            RenderHelper.vertex(0.4, 1 - (depth / 16D), 0.2);
            RenderHelper.vertex(0.4, 1, 0.2);
            RenderHelper.vertex(1, 1, 0.5);
        }

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(0.4, 1 - (depth / 16D), 0.2);
            RenderHelper.vertex(0.4, 1 - (depth / 16D), 0.4);
            RenderHelper.vertex(0.4, 1, 0.4);
            RenderHelper.vertex(0.4, 1, 0.2);
        }

        GL11.glNormal3d(0.5, 0, 0.5);
        {
            RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.4);
            RenderHelper.vertex(0, 1 - (depth / 16D), 0.1);
            RenderHelper.vertex(0, 1, 0.1);
            RenderHelper.vertex(0.5, 1, 0.4);
        }

        GL11.glEnd();

        GL11.glDisable(GL11.GL_CLIP_PLANE0);
    }
}
