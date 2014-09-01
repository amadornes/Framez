package com.amadornes.framez.client;

import java.nio.DoubleBuffer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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

    private static RenderItem customRenderItem;

    public static void renderItem(ItemStack item) {

        if (customRenderItem == null) {
            customRenderItem = new RenderItem() {

                @Override
                public boolean shouldSpreadItems() {

                    return false;
                }
            };

            customRenderItem.setRenderManager(RenderManager.instance);
        }

        EntityItem ghostEntityItem = new EntityItem(Minecraft.getMinecraft().theWorld);
        ghostEntityItem.hoverStart = 0.0F;
        ghostEntityItem.setEntityItemStack(item);

        GL11.glColor3d(1, 1, 1);

        if (item.getItem() instanceof ItemBlock) {
            ItemBlock testItem = (ItemBlock) item.getItem();
            Block testBlock = testItem.field_150939_a;
            if (RenderBlocks.renderItemIn3d(testBlock.getRenderType())) {
                GL11.glScaled(1.2, 1.2, 1.2);
            }
        }

        customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
    }

    public static DoubleBuffer planeEquation(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {

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

}
