package com.amadornes.framez.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMotor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        renderer.renderAllFaces = true;

        // Bottom
        renderer.setRenderBounds(0, 0, 0, 1 / 16D, 1 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(15 / 16D, 0, 0, 1, 1 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(1 / 16D, 0, 0, 15 / 16D, 1 / 16D, 1 / 16D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(1 / 16D, 0, 15 / 16D, 15 / 16D, 1 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);

        // Top
        renderer.setRenderBounds(0, 15 / 16D, 0, 1 / 16D, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(15 / 16D, 15 / 16D, 0, 1, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(1 / 16D, 15 / 16D, 0, 15 / 16D, 1, 1 / 16D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);

        // Sides
        renderer.setRenderBounds(0, 1 / 16D, 0, 1 / 16D, 15 / 16D, 1 / 16D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(15 / 16D, 1 / 16D, 0, 1, 15 / 16D, 1 / 16D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(15 / 16D, 1 / 16D, 15 / 16D, 1, 15 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setRenderBounds(0, 1 / 16D, 15 / 16D, 1 / 16D, 15 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);

        // Center
        renderer.setRenderBounds(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        TileMotor te = (TileMotor) tile;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            GL11.glPushMatrix();
            {
                if (te.getStructure() != null)
                    renderStructure(te, frame);
            }
            GL11.glPopMatrix();

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

    private static RenderBlocks rb = new RenderBlocks();

    public void renderStructure(TileMotor motor, float frame) {

        MovingStructure structure = motor.getStructure();

        if (structure.getBlocks() == null)
            return;
        List<MovingBlock> blocks = new ArrayList<MovingBlock>(structure.getBlocks());

        GL11.glTranslated(structure.getDirection().offsetX * structure.getMoved(), structure.getDirection().offsetY * structure.getMoved(),
                structure.getDirection().offsetZ * structure.getMoved());
        GL11.glTranslated(structure.getDirection().offsetX * (structure.getSpeed() * frame), structure.getDirection().offsetY
                * (structure.getSpeed() * frame), structure.getDirection().offsetZ * (structure.getSpeed() * frame));
        GL11.glTranslated(-motor.xCoord, -motor.yCoord, -motor.zCoord);

        // ISBRH
        {
            GL11.glPushMatrix();

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            rb.blockAccess = structure.getWorldWrapper();

            for (MovingBlock b : blocks) {
                if (b == null)
                    continue;
                Tessellator.instance.startDrawingQuads();
                if (b.getBlock() != null && b.getLocation() != null) {
                    for (int pass = 0; pass < 2; pass++) {
                        if (b.getBlock().canRenderInPass(pass)) {
                            rb.renderBlockByRenderType(b.getBlock(), b.getLocation().x, b.getLocation().y, b.getLocation().z);
                        }
                    }
                }
                Tessellator.instance.draw();
            }

            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

            GL11.glPopMatrix();
        }

        // TESR
        {
            GL11.glPushMatrix();
            for (MovingBlock b : blocks) {
                if (b == null)
                    continue;

                TileEntity te = b.getTileEntity();
                if (te == null)
                    continue;

                GL11.glTranslated(b.getLocation().x, b.getLocation().y, b.getLocation().z);

                TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(te);
                if (tesr != null) {
                    GL11.glPushMatrix();
                    tesr.renderTileEntityAt(te, 0, 0, 0, frame);
                    GL11.glPopMatrix();
                }
            }
            GL11.glPopMatrix();
        }
    }
}
