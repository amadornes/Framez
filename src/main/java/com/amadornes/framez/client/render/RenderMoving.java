package com.amadornes.framez.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMoving;

public class RenderMoving extends TileEntitySpecialRenderer {

    private static RenderBlocks rb = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {

        GL11.glPushMatrix();
        {
            TileMoving te = (TileMoving) tile;
            MovingBlock b = te.getBlockA();

            if (b != null) {

                MovingStructure structure = b.getStructure();

                Minecraft mc = Minecraft.getMinecraft();

                GL11.glTranslated(x, y, z);
                GL11.glTranslated(-b.getX(), -b.getY(), -b.getZ());

                double m = structure.getMoved(-f * 0.75F);
                double x1 = structure.getDirection().offsetX * m;
                double y1 = structure.getDirection().offsetY * m;
                double z1 = structure.getDirection().offsetZ * m;
                GL11.glTranslated(x1, y1, z1);

                // Save old world instances
                World playerWorld = mc.thePlayer.worldObj;
                WorldClient gameWorld = mc.theWorld;

                // Set new world instances
                mc.thePlayer.worldObj = structure.getWorldWrapperClient();
                mc.theWorld = structure.getWorldWrapperClient();

                // Render
                {
                    GL11.glPushMatrix();

                    RenderHelper.disableStandardItemLighting();

                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    rb.blockAccess = structure.getWorldWrapperClient();

                    World w = null;
                    TileEntity blockTE = b.getTileEntity();
                    if (blockTE != null) {
                        w = blockTE.getWorldObj();
                        blockTE.setWorldObj(structure.getWorldWrapper());
                    }

                    // ISBRH
                    if (b.getRenderList() == -1) {
                        int l = 0;
                        b.setRenderList(l = GL11.glGenLists(1));
                        GL11.glNewList(l, GL11.GL_COMPILE);
                        boolean crashed = false;
                        try {
                            if (b.getBlock() != null && b.getLocation() != null) {
                                for (int pass = 0; pass < 2; pass++) {
                                    if (b.getBlock().canRenderInPass(pass)) {
                                        try {
                                            Tessellator.instance.startDrawingQuads();
                                            rb.renderBlockByRenderType(b.getBlock(), b.getX(), b.getY(), b.getZ());
                                            Tessellator.instance.draw();
                                        } catch (Exception e) {
                                            try {
                                                Tessellator.instance.draw();
                                            } catch (Exception ex) {
                                            }
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            crashed = true;
                        }
                        GL11.glEndList();
                        if (crashed) {
                            GL11.glNewList(l, GL11.GL_COMPILE);
                            rb.renderStandardBlock(Blocks.stone, b.getX(), b.getY(), b.getZ());
                            GL11.glEndList();
                        }
                    }
                    GL11.glCallList(b.getRenderList());

                    RenderHelper.enableStandardItemLighting();

                    // TESR
                    if (blockTE != null) {
                        TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(blockTE);
                        if (tesr != null) {
                            GL11.glPushMatrix();
                            try {
                                tesr.renderTileEntityAt(blockTE, b.getX(), b.getY(), b.getZ(), f);
                            } catch (Exception ex) {
                                rb.renderStandardBlock(Blocks.stone, b.getX(), b.getY(), b.getZ());
                            }
                            GL11.glPopMatrix();
                        }
                    }

                    if (blockTE != null)
                        blockTE.setWorldObj(w);

                    GL11.glPopMatrix();
                }

                // Reset world instances
                mc.thePlayer.worldObj = playerWorld;
                mc.theWorld = gameWorld;
            }
        }
        GL11.glPopMatrix();
    }

}
