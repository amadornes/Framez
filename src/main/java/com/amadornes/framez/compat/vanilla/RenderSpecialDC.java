package com.amadornes.framez.compat.vanilla;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.client.render.IRenderMotorSpecial;
import com.amadornes.framez.tile.TileMotor;

public class RenderSpecialDC implements IRenderMotorSpecial {

    @Override
    public boolean shouldRender(TileMotor motor, ForgeDirection face) {

        return motor instanceof TileMotorDC && face == ForgeDirection.UP;
    }

    @Override
    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame) {

        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;

        float brightness = ((System.currentTimeMillis() % 2000) / 1000F);
        if (brightness > 1)
            brightness = 1 - (brightness - 1);
        brightness = (brightness * 0.75F * (motor.getWorldObj().getBlockPowerInput(motor.xCoord, motor.yCoord, motor.zCoord) / 16F)) + 0.25F;
        brightness *= 240F;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glRotated(90, 0, 1, 0);
        GL11.glTranslated(-0.5, -0.5, -0.5);

        GL11.glTranslated(0.125, 0, 0.125);
        GL11.glScaled(0.75, 1, 0.75);

        GL11.glColor4d(1, 0, 0, 1);
        GL11.glNormal3d(0, 1, 0);

        double depth = 1;

        GL11.glBegin(GL11.GL_POLYGON);
        {
            RenderHelper.vertex(8 / 16D, 1, 8 / 16D);

            RenderHelper.vertex(7 / 16D, 1, 3 / 16D);//
            RenderHelper.vertex(7 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(6 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(6 / 16D, 1, 5 / 16D);//
            RenderHelper.vertex(5 / 16D, 1, 5 / 16D);//
            RenderHelper.vertex(5 / 16D, 1, 6 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 6 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(2 / 16D, 1, 11 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 11 / 16D);//
            RenderHelper.vertex(3 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(4 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(6 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(6 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(10 / 16D, 1, 14 / 16D);//
            RenderHelper.vertex(10 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 13 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 12 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 11 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 11 / 16D);//
            RenderHelper.vertex(14 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 8 / 16D);//
            RenderHelper.vertex(13 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 7 / 16D);//
            RenderHelper.vertex(12 / 16D, 1, 6 / 16D);//
            RenderHelper.vertex(11 / 16D, 1, 6 / 16D);//
            RenderHelper.vertex(11 / 16D, 1, 5 / 16D);//
            RenderHelper.vertex(10 / 16D, 1, 5 / 16D);//
            RenderHelper.vertex(10 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(9 / 16D, 1, 4 / 16D);//
            RenderHelper.vertex(9 / 16D, 1, 3 / 16D);
            RenderHelper.vertex(7 / 16D, 1, 3 / 16D);
        }
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(7 / 16D, 1, 3 / 16D);
            RenderHelper.vertex(7 / 16D, 1 - (depth / 16D), 3 / 16D);
            RenderHelper.vertex(7 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(7 / 16D, 1, 4 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(7 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(7 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 4 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(6 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 5 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(6 / 16D, 1, 5 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(5 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(5 / 16D, 1, 5 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(5 / 16D, 1, 5 / 16D);
            RenderHelper.vertex(5 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(5 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(5 / 16D, 1, 6 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(5 / 16D, 1, 6 / 16D);
            RenderHelper.vertex(5 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 6 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(4 / 16D, 1, 6 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 7 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 7 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(3 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 8 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(3 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 8 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(2 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(2 / 16D, 1, 11 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(2 / 16D, 1, 11 / 16D);
            RenderHelper.vertex(2 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 11 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(3 / 16D, 1, 11 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(3 / 16D, 1, 12 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(3 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(3 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 12 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(4 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(4 / 16D, 1, 13 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 13 / 16D);
        }
        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(6 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(6 / 16D, 1, 14 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(6 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(6 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 14 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(10 / 16D, 1, 14 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 14 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 13 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(10 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 13 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(12 / 16D, 1, 13 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 13 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 12 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(12 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 12 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(13 / 16D, 1, 12 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 12 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 11 / 16D);
        }
        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(13 / 16D, 1, 11 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 11 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(14 / 16D, 1, 11 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 11 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(14 / 16D, 1, 8 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(14 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(14 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 8 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(13 / 16D, 1, 8 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 8 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(13 / 16D, 1, 7 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(13 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(13 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 7 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(12 / 16D, 1, 7 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 7 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(12 / 16D, 1, 6 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(12 / 16D, 1, 6 / 16D);
            RenderHelper.vertex(12 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(11 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(11 / 16D, 1, 6 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(11 / 16D, 1, 6 / 16D);
            RenderHelper.vertex(11 / 16D, 1 - (depth / 16D), 6 / 16D);
            RenderHelper.vertex(11 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(11 / 16D, 1, 5 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(11 / 16D, 1, 5 / 16D);
            RenderHelper.vertex(11 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 5 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(10 / 16D, 1, 5 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 5 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(10 / 16D, 1, 4 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(10 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(10 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(9 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(9 / 16D, 1, 4 / 16D);
        }
        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex(9 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(9 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(9 / 16D, 1 - (depth / 16D), 3 / 16D);
            RenderHelper.vertex(9 / 16D, 1, 3 / 16D);
        }
        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(9 / 16D, 1, 3 / 16D);
            RenderHelper.vertex(9 / 16D, 1 - (depth / 16D), 3 / 16D);
            RenderHelper.vertex(7 / 16D, 1 - (depth / 16D), 3 / 16D);
            RenderHelper.vertex(7 / 16D, 1, 3 / 16D);
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
    }

}
