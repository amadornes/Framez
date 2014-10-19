package com.amadornes.framez.client.render;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    public static void vertex(double x, double y, double z) {

        GL11.glVertex3d(x, y, z);
    }

    public static void vertexWithTexture(double x, double y, double z, double tx, double ty) {

        GL11.glTexCoord2d(tx, ty);
        GL11.glVertex3d(x, y, z);
    }

    public static DoubleBuffer planeEquation(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3) {

        double[] eq = new double[4];
        eq[0] = (y1 * (z2 - z3)) + (y2 * (z3 - z1)) + (y3 * (z1 - z2));
        eq[1] = (z1 * (x2 - x3)) + (z2 * (x3 - x1)) + (z3 * (x1 - x2));
        eq[2] = (x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2));
        eq[3] = -((x1 * ((y2 * z3) - (y3 * z2))) + (x2 * ((y3 * z1) - (y1 * z3))) + (x3 * ((y1 * z2) - (y2 * z1))));
        return fromArray(eq);
    }

    public static DoubleBuffer fromArray(double... eq) {

        DoubleBuffer b = BufferUtils.createDoubleBuffer(eq.length * 2).put(eq);
        b.flip();
        return b;
    }

    public static void drawColoredCube() {

        // Top side
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        GL11.glVertex3f(0, 1, 1);
        GL11.glVertex3f(1, 1, 1);
        GL11.glVertex3f(1, 1, 0);
        GL11.glVertex3f(0, 1, 0);

        // Bottom side
        GL11.glColor3f(1.0F, 1.0F, 0.0F);
        GL11.glVertex3f(1, 0, 1);
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(1, 0, 0);

        // Draw west side:
        GL11.glColor3f(0.0F, 1.0F, 0.0F);
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(0, 1, 1);
        GL11.glVertex3f(0, 1, 0);
        GL11.glVertex3f(0, 0, 0);

        // Draw east side:
        GL11.glColor3f(0.0F, 1.0F, 1.0F);
        GL11.glVertex3f(1, 0, 0);
        GL11.glVertex3f(1, 1, 0);
        GL11.glVertex3f(1, 1, 1);
        GL11.glVertex3f(1, 0, 1);

        // Draw north side
        GL11.glColor3f(0.0F, 0.0F, 1.0F);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 1, 0);
        GL11.glVertex3f(1, 1, 0);
        GL11.glVertex3f(1, 0, 0);

        // Draw south side
        GL11.glColor3f(0.0F, 0.0F, 0.0F);
        GL11.glVertex3f(0, 0, 1);
        GL11.glVertex3f(1, 0, 1);
        GL11.glVertex3f(1, 1, 1);
        GL11.glVertex3f(0, 1, 1);
    }

}
