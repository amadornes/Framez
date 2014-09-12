package com.amadornes.framez.compat.pc;

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

public class RenderSpecialPC implements IRenderMotorSpecial {

    @Override
    public boolean shouldRender(TileMotor motor, ForgeDirection face) {

        return motor instanceof TileMotorPC && face == ForgeDirection.UP;
    }

    @Override
    public boolean shouldRender(ItemStack item, ForgeDirection face) {

        if (item.getItem() instanceof ItemBlock) {
            Block b = Block.getBlockFromItem(item.getItem());
            if (b instanceof IMotor && ((IMotor) b).getProvider() instanceof MotorProviderPC) {
                return face == ForgeDirection.UP;
            }
        }

        return false;
    }

    @Override
    public void renderSpecial(TileMotor motor, ForgeDirection face, float frame) {

        TileMotorPC te = (TileMotorPC) motor;

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

        GL11.glColor4d(1, 0, 0, 1);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);
        renderPressureGauge(false, powerPercentage);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20, 20);
        renderPressureGauge(false, -powerPercentage);

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

        GL11.glTranslated(0.125, 0, 0.125);
        GL11.glScaled(0.75, 1, 0.75);

        GL11.glColor4d(1, 0, 0, 1);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20, 20);
        renderPressureGauge(false, 0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
    }

    public void renderPressureGauge(boolean inverted, double powerPercentage) {

        double powerPercentageBorder = Math.copySign((Math.abs(powerPercentage) * 0.57) + 0.25, powerPercentage);

        double p = Math.abs(powerPercentageBorder) * 1.001 - 0.0005;

        GL11.glTranslated(p, 0, 0);
        if (powerPercentageBorder > 0) {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 1, 0, 0, 0, 0, 0, 1, 1));
        } else {
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 0, 0, 0, 1, 0, 0, 1, 1));
        }
        GL11.glTranslated(-p, 0, 0);

        GL11.glEnable(GL11.GL_CLIP_PLANE0);

        double depth = 1;

        GL11.glNormal3d(0, 1, 0);
        GL11.glBegin(GL11.GL_QUADS);
        {
            RenderHelper.vertex(4 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(4 / 16D, 1, (4 + 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (4 + 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, 4 / 16D);

            RenderHelper.vertex(4 / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex(4 / 16D, 1, (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4 - 1) / 16D);

            RenderHelper.vertex(4 / 16D, 1, (4 + 1) / 16D);
            RenderHelper.vertex(4 / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((4 + 1) / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((4 + 1) / 16D, 1, (4 + 1) / 16D);

            RenderHelper.vertex((16 - 4 - 1) / 16D, 1, (4 + 1) / 16D);
            RenderHelper.vertex((16 - 4 - 1) / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (4 + 1) / 16D);
        }
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex(4 / 16D, 1, 4 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex(4 / 16D, 1, (16 - 4) / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex((16 - 4) / 16D, 1, 4 / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), 4 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, 4 / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, 4 / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), 4 / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, (16 - 4) / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4) / 16D);
        }

        // --------------------------------

        GL11.glNormal3d(0, 0, -1);
        {
            RenderHelper.vertex((16 - 4 - 1) / 16D, 1, 4 / 16D);
            RenderHelper.vertex((16 - 4 - 1) / 16D, 1 - (depth / 16D), 4 / 16D);
            RenderHelper.vertex((16 - 4 - 1) / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex((16 - 4 - 1) / 16D, 1, (16 - 4) / 16D);
        }

        GL11.glNormal3d(0, 0, 1);
        {
            RenderHelper.vertex((4 + 1) / 16D, 1, 4 / 16D);
            RenderHelper.vertex((4 + 1) / 16D, 1, (16 - 4) / 16D);
            RenderHelper.vertex((4 + 1) / 16D, 1 - (depth / 16D), (16 - 4) / 16D);
            RenderHelper.vertex((4 + 1) / 16D, 1 - (depth / 16D), 4 / 16D);
        }

        GL11.glNormal3d(1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (16 - 4 - 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), (16 - 4 - 1) / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), (16 - 4 - 1) / 16D);
        }

        GL11.glNormal3d(-1, 0, 0);
        {
            RenderHelper.vertex(4 / 16D, 1, (4 + 1) / 16D);
            RenderHelper.vertex(4 / 16D, 1 - (depth / 16D), (4 + 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1 - (depth / 16D), (4 + 1) / 16D);
            RenderHelper.vertex((16 - 4) / 16D, 1, (4 + 1) / 16D);
        }

        GL11.glEnd();

        GL11.glDisable(GL11.GL_CLIP_PLANE0);

        if (powerPercentage >= 0) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

            GL11.glPushMatrix();
            {
                double angle = (90 + 45) - ((360 - 90) * powerPercentage * 1.14);

                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glRotated(angle, 0, 1, 0);
                GL11.glTranslated(-0.5, 0, -0.5);

                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_TRIANGLES);
                {
                    RenderHelper.vertex(0.5, 1, 0.5 - 0.025);
                    RenderHelper.vertex(0.5, 1, 0.5 + 0.025);
                    RenderHelper.vertex(0.75 - 1 / 16D - 0.005, 1, 0.5);
                }
                GL11.glEnd();

                GL11.glBegin(GL11.GL_QUADS);

                GL11.glNormal3d(0, 0, -1);
                {
                    RenderHelper.vertex(0.5, 1, 0.5 - 0.025);
                    RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5 - 0.025);
                    RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5 + 0.025);
                    RenderHelper.vertex(0.5, 1, 0.5 + 0.025);
                }

                GL11.glNormal3d(-1, 0, 1);
                {
                    RenderHelper.vertex(0.5, 1, 0.5 + 0.025);
                    RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5 + 0.025);
                    RenderHelper.vertex(0.75 - 1 / 16D - 0.005, 1 - (depth / 16D), 0.5);
                    RenderHelper.vertex(0.75 - 1 / 16D - 0.005, 1, 0.5);
                }

                GL11.glNormal3d(1, 0, 1);
                {
                    RenderHelper.vertex(0.75 - 1 / 16D - 0.005, 1, 0.5);
                    RenderHelper.vertex(0.75 - 1 / 16D - 0.005, 1 - (depth / 16D), 0.5);
                    RenderHelper.vertex(0.5, 1 - (depth / 16D), 0.5 - 0.025);
                    RenderHelper.vertex(0.5, 1, 0.5 - 0.025);
                }

                GL11.glEnd();
            }
            GL11.glPopMatrix();
        }
    }
}
