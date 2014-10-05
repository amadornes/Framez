package com.amadornes.framez.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IRenderMotorSpecial;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMotor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    public static boolean renderingBorder = false;

    private RenderBlocks rb = new RenderBlocks();

    private boolean itemRenderer = false;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    private void renderStandardBlock(Block block, int x, int y, int z) {

        if (!itemRenderer) {
            rb.renderStandardBlock(block, x, y, z);
        } else {
            if (renderingBorder)
                Tessellator.instance.setColorOpaque_F(0.8F, 0.1F, 0.1F);

            Tessellator.instance.setNormal((float) -rb.renderMinX, 0, 0);
            rb.renderFaceXNeg(block, x, y, z, block.getIcon(ForgeDirection.WEST.ordinal(), 0));
            Tessellator.instance.setNormal((float) rb.renderMaxX, 0, 0);
            rb.renderFaceXPos(block, x, y, z, block.getIcon(ForgeDirection.EAST.ordinal(), 0));
            Tessellator.instance.setNormal(0, (float) -rb.renderMinY, 0);
            rb.renderFaceYNeg(block, x, y, z, block.getIcon(ForgeDirection.DOWN.ordinal(), 0));
            Tessellator.instance.setNormal(0, (float) rb.renderMaxY, 0);
            rb.renderFaceYPos(block, x, y, z, block.getIcon(ForgeDirection.UP.ordinal(), 0));
            Tessellator.instance.setNormal(0, 0, (float) -rb.renderMinZ);
            rb.renderFaceZNeg(block, x, y, z, block.getIcon(ForgeDirection.NORTH.ordinal(), 0));
            Tessellator.instance.setNormal(0, 0, (float) rb.renderMaxZ);
            rb.renderFaceZPos(block, x, y, z, block.getIcon(ForgeDirection.SOUTH.ordinal(), 0));

            if (renderingBorder)
                Tessellator.instance.setColorOpaque_F(1, 1, 1);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        RenderBlocks rbOld = rb;
        rb = renderer;
        render(block, x, y, z);
        rb = rbOld;

        return true;
    }

    private void render(Block block, int x, int y, int z) {

        rb.renderAllFaces = true;

        renderingBorder = true;

        // Bottom
        rb.setRenderBounds(0, 0, 0, 1 / 16D, 1 / 16D, 1);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(15 / 16D, 0, 0, 1, 1 / 16D, 1);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(1 / 16D, 0, 0, 15 / 16D, 1 / 16D, 1 / 16D);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(1 / 16D, 0, 15 / 16D, 15 / 16D, 1 / 16D, 1);
        renderStandardBlock(block, x, y, z);

        // Top
        rb.setRenderBounds(0, 15 / 16D, 0, 1 / 16D, 1, 1);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(15 / 16D, 15 / 16D, 0, 1, 1, 1);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(1 / 16D, 15 / 16D, 0, 15 / 16D, 1, 1 / 16D);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D, 1, 1);
        renderStandardBlock(block, x, y, z);

        // Sides
        rb.setRenderBounds(0, 1 / 16D, 0, 1 / 16D, 15 / 16D, 1 / 16D);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(15 / 16D, 1 / 16D, 0, 1, 15 / 16D, 1 / 16D);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(15 / 16D, 1 / 16D, 15 / 16D, 1, 15 / 16D, 1);
        renderStandardBlock(block, x, y, z);
        rb.setRenderBounds(0, 1 / 16D, 15 / 16D, 1 / 16D, 15 / 16D, 1);
        renderStandardBlock(block, x, y, z);

        renderingBorder = false;

        // Center
        rb.setRenderBounds(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D);
        renderStandardBlock(block, x, y, z);

        rb.renderAllFaces = false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        itemRenderer = true;

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {

            switch (type) {
            case ENTITY:
                GL11.glTranslated(-0.5, 0, -0.5);
                if (item.getItemFrame() != null)
                    GL11.glTranslated(0, -0.5, 0);
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                break;
            case INVENTORY:
                GL11.glTranslated(0, -0.0625, 0);
                break;
            default:
                break;
            }

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(-90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Tessellator.instance.startDrawingQuads();
            render(Block.getBlockFromItem(item.getItem()), 0, 0, 0);
            Tessellator.instance.draw();

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPushMatrix();
            {
                renderArrow(false, 0);
                renderArrow(true, 0);
            }
            GL11.glPopMatrix();

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            for (ForgeDirection f : ForgeDirection.VALID_DIRECTIONS) {
                GL11.glPushMatrix();

                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                switch (f) {
                case UP:
                    break;
                case DOWN:
                    GL11.glRotated(180, 1, 0, 0);
                    break;
                case WEST:
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case EAST:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case NORTH:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case SOUTH:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                default:
                    break;
                }
                GL11.glTranslated(-0.5, -0.5, -0.5);

                IRenderMotorSpecial[] l = FramezApi.inst().getMotorRegistry().getRenderers(item, f);
                for (IRenderMotorSpecial r : l) {
                    GL11.glPushMatrix();
                    r.renderSpecial(item, f, 0);
                    GL11.glPopMatrix();
                }

                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();

        itemRenderer = false;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        TileMotor te = (TileMotor) tile;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            ForgeDirection face = te.getFace();
            ForgeDirection direction = te.getDirection();
            GL11.glTranslated(0.5, 0.5, 0.5);
            switch (face) {
            case UP:
                GL11.glRotated(90, 1, 0, 0);
                switch (direction) {
                case WEST:
                    break;
                case EAST:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case NORTH:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case SOUTH:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                default:
                    break;
                }
                break;
            case DOWN:
                GL11.glRotated(-90, 1, 0, 0);
                switch (direction) {
                case WEST:
                    break;
                case EAST:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case NORTH:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case SOUTH:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                default:
                    break;
                }
                break;
            case WEST:
                GL11.glRotated(90, 0, 1, 0);
                switch (direction) {
                case UP:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case DOWN:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case NORTH:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case SOUTH:
                    break;
                default:
                    break;
                }
                break;
            case EAST:
                GL11.glRotated(-90, 0, 1, 0);
                switch (direction) {
                case UP:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case DOWN:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case NORTH:
                    break;
                case SOUTH:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                default:
                    break;
                }
                break;
            case NORTH:
                switch (direction) {
                case UP:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case DOWN:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case EAST:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case WEST:
                    // Do nothing
                    break;
                default:
                    break;
                }
                break;
            case SOUTH:
                GL11.glRotated(180, 0, 1, 0);
                switch (direction) {
                case UP:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case DOWN:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case EAST:
                    // Do nothing
                    break;
                case WEST:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                default:
                    break;
                }
                break;
            default:
                break;
            }
            GL11.glTranslated(-0.5, -0.5, -0.5);

            GL11.glPushMatrix();
            {
                double progress = te.getMoved();
                renderArrow(false, progress);
                renderArrow(true, progress);
                renderArrow(false, -progress);
                renderArrow(true, -progress);
            }
            GL11.glPopMatrix();

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            for (ForgeDirection f : ForgeDirection.VALID_DIRECTIONS) {
                GL11.glPushMatrix();

                GL11.glTranslated(0.5, 0.5, 0.5);
                switch (f) {
                case UP:
                    break;
                case DOWN:
                    GL11.glRotated(180, 1, 0, 0);
                    break;
                case WEST:
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case EAST:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case NORTH:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                case SOUTH:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                default:
                    break;
                }
                GL11.glTranslated(-0.5, -0.5, -0.5);

                IRenderMotorSpecial[] l = FramezApi.inst().getMotorRegistry().getRenderers(te, f);
                for (IRenderMotorSpecial r : l) {
                    GL11.glPushMatrix();
                    r.renderSpecial(te, f, frame);
                    GL11.glPopMatrix();
                }

                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();

    }

    public void renderArrow(boolean direction, double progress) {

        double distSides = 4.5;
        double distBottom = 3;
        double thickness = 1.5;
        double depth = 1;

        GL11.glPushMatrix();
        {
            if (progress <= 0) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 20f, 20f);
            } else {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            }

            if (direction) {
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 0, 1);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);
                GL11.glTranslated(0, 0, 1 - ((distBottom + (thickness / 2D)) / 8D));
            }

            double p = (1 - (Math.abs(progress) * ((16 - (distSides * 2)) / 16D) + (distSides / 16D))) * 1.001;

            GL11.glTranslated(p, 0, 0);
            if (progress <= 0) {
                GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 1, 0, 0, 0, 0, 0, 1, 1));
            } else {
                GL11.glClipPlane(GL11.GL_CLIP_PLANE0, RenderHelper.planeEquation(0, 0, 0, 0, 1, 0, 0, 1, 1));
            }
            GL11.glTranslated(-p, 0, 0);

            GL11.glEnable(GL11.GL_CLIP_PLANE0);

            if (progress < 0)
                progress = Math.abs(progress + 1);

            GL11.glColor4d(1, 0, 0, 1);

            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glNormal3d(0, 1, 0);
            GL11.glBegin(GL11.GL_POLYGON);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, distBottom / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom - (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides) / 16D, 1, (distBottom + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1, (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1, distBottom / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(1, 0, 0);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, distBottom / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), distBottom / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom - (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom - (thickness / 2D)) / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(-1, 0, -1);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom - (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom - (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides) / 16D, 1 - (depth / 16D), (distBottom + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides) / 16D, 1, (distBottom + (thickness / 2D)) / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(-1, 0, 1);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides) / 16D, 1, (distBottom + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides) / 16D, 1 - (depth / 16D), (distBottom + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom + thickness + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness + (thickness / 2D)) / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(1, 0, 0);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom + thickness + (thickness / 2D)) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom + thickness) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness) / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(0, 0, 1);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, (distBottom + thickness) / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1 - (depth / 16D), (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1, (distBottom + thickness) / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(1, 0, 0);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex(1 - (distSides / 16D), 1, (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1 - (depth / 16D), (distBottom + thickness) / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1 - (depth / 16D), distBottom / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1, distBottom / 16D);
            }
            GL11.glEnd();

            GL11.glNormal3d(0, 0, -1);
            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.vertex((distSides + thickness) / 16D, 1, distBottom / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1, distBottom / 16D);
                RenderHelper.vertex(1 - (distSides / 16D), 1 - (depth / 16D), distBottom / 16D);
                RenderHelper.vertex((distSides + thickness) / 16D, 1 - (depth / 16D), distBottom / 16D);
            }
            GL11.glEnd();

            GL11.glDisable(GL11.GL_CLIP_PLANE0);

            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glPopMatrix();
    }

}
