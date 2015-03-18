package com.amadornes.framez.client;

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

import uk.co.qmunity.lib.vec.Vec3d;

import com.amadornes.framez.Framez;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.util.Timing;
import com.amadornes.framez.world.FakeWorldClient;

public class RenderMoving extends TileEntitySpecialRenderer {

    private static RenderBlocks rb = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {

        Framez.proxy.setFrame(f);

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();
        {
            TileMoving te = (TileMoving) tile;
            MovingBlock b = te.getBlockA();

            if (b != null) {
                MovingStructure structure = b.getStructure();

                Minecraft mc = Minecraft.getMinecraft();

                GL11.glTranslated(x, y, z);
                GL11.glTranslated(-b.getX(), -b.getY(), -b.getZ());

                Vec3d t = structure.getMovement().transform(new Vec3d(b.getX(), b.getY(), b.getZ()),
                        Math.min(structure.getInterpolatedProgress(Framez.proxy.getFrame() - (1 + (1 - Timing.SECONDS))), 1));
                GL11.glTranslated(-b.getX(), -b.getY(), -b.getZ());
                GL11.glTranslated(t.getX(), t.getY(), t.getZ());

                // Save old world instances
                World playerWorld = mc.thePlayer.worldObj;
                WorldClient gameWorld = mc.theWorld;

                // Set new world instances
                mc.thePlayer.worldObj = FakeWorldClient.instance(structure);
                mc.theWorld = FakeWorldClient.instance(structure);

                boolean rendered = false;

                // Render
                {
                    GL11.glPushMatrix();

                    RenderHelper.disableStandardItemLighting();

                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    rb.blockAccess = FakeWorldClient.instance(structure);

                    World w = null;
                    TileEntity blockTE = b.getTileEntity();
                    if (blockTE != null) {
                        w = blockTE.getWorldObj();
                        blockTE.setWorldObj(FakeWorldClient.instance(structure));
                    }

                    // ISBRH
                    if (b.getRenderList() == -1) {
                        int l = 0;
                        b.setRenderList(l = GL11.glGenLists(1));
                        GL11.glNewList(l, GL11.GL_COMPILE);
                        Tessellator.instance.startDrawingQuads();
                        for (int pass = 0; pass < 2; pass++) {
                            if (b.getBlock().canRenderInPass(pass)) {
                                try {
                                    if (rb.renderBlockByRenderType(b.getBlock(), b.getX(), b.getY(), b.getZ()))
                                        rendered = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    rendered = false;
                                    break;
                                }
                            }
                        }
                        Tessellator.instance.draw();
                        GL11.glEndList();

                        if (!rendered) {
                            GL11.glDeleteLists(l, 1);
                            b.setRenderList(-2);
                        }
                    } else {
                        rendered = b.getRenderList() > 0;
                    }
                    if (rendered)
                        GL11.glCallList(b.getRenderList());

                    RenderHelper.enableStandardItemLighting();

                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                    // TESR
                    if (blockTE != null) {
                        TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(blockTE);
                        if (tesr != null) {
                            GL11.glPushMatrix();
                            try {
                                tesr.renderTileEntityAt(blockTE, b.getX(), b.getY(), b.getZ(), f);
                                rendered = true;
                            } catch (Exception ex) {
                            }
                            GL11.glPopMatrix();
                        }
                    }

                    if (blockTE != null)
                        blockTE.setWorldObj(w);

                    GL11.glPopMatrix();
                }

                if (b.getBlock() == Blocks.air)
                    rendered = true;

                // If there were any issues, render the crate
                if (!rendered) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                    RenderHelper.disableStandardItemLighting();
                    Tessellator.instance.startDrawingQuads();
                    rb.blockAccess = structure.getWorld();
                    rb.overrideBlockTexture = IconSupplier.wood_simple;
                    rb.setRenderBounds(0, 0, 0, 1, 1, 1);
                    rb.setRenderAllFaces(true);
                    rb.renderStandardBlock(Blocks.stone, b.getX(), b.getY(), b.getZ());
                    rb.overrideBlockTexture = null;
                    rb.blockAccess = FakeWorldClient.instance(structure);
                    Tessellator.instance.draw();
                    RenderHelper.enableStandardItemLighting();
                }

                // Reset world instances
                mc.thePlayer.worldObj = playerWorld;
                mc.theWorld = gameWorld;

                if (b.getStructure().getProgress() >= 1 + b.getStructure().getSpeed()) {
                    GL11.glDeleteLists(b.getRenderList(), 1);
                    b.setRenderList(-2);
                }
            }
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
    }
}
