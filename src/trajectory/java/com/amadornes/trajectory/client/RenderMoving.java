package com.amadornes.trajectory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.block.TileMoving;
import com.amadornes.trajectory.movement.MovingStructure;
import com.amadornes.trajectory.util.EventHelper;
import com.amadornes.trajectory.world.FakeWorld;

public class RenderMoving extends TileEntitySpecialRenderer {

    private static RenderBlocks rb = new RenderBlocks();
    public static int pass = 0;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {

        TileMoving te = (TileMoving) tile;
        MovingStructure structure = te.structure;
        if (structure == null)
            return;
        if (structure.getRenderingTile() != tile)
            return;
        FakeWorld.getFakeWorld(structure);

        BlockPos min = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (IMovingBlock b : structure.getBlocks())
            min.set(Math.min(min.x, b.getPosition().x), Math.min(min.y, b.getPosition().y), Math.min(min.z, b.getPosition().z));

        if (structure.needsReRender())
            preRenderStructure(min, structure);

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x - te.xCoord + min.x, y - te.yCoord + min.y, z - te.zCoord + min.z);
            structure.getTrajectory().transformGL(new Vector3(min), structure.getBlocks(), structure.getTicksMoved() + f);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            int l = structure.getRenderList();
            RenderHelper.disableStandardItemLighting();
            if (l > 0) {
                if (pass == 0) {
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glCallList(l + 0);// Pass 0
                    GL11.glEnable(GL11.GL_BLEND);
                } else if (pass == 1) {
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glCallList(l + 1);// Pass 1
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
            RenderHelper.enableStandardItemLighting();

            for (IMovingBlock b : structure.getBlocks()) {
                if (EventHelper.fireRenderDynamicEvent(b, structure.getTrajectory(), pass, f))
                    continue;
                TileEntity movingTile = b.getTileEntity();
                if (movingTile != null) {
                    try {
                        if (movingTile.shouldRenderInPass(pass))
                            TileEntityRendererDispatcher.instance.renderTileEntityAt(movingTile, te.xCoord - tile.xCoord, te.yCoord
                                    - tile.yCoord, te.zCoord - tile.zCoord, f);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        GL11.glPopMatrix();
    }

    private void preRenderStructure(BlockPos min, MovingStructure structure) {

        rb.blockAccess = structure.getFakeWorld();

        int l;
        if ((l = structure.getRenderList()) < 0)
            structure.setRenderList(l = GL11.glGenLists(2));

        boolean rendered = false;

        for (int pass = 0; pass < 2; pass++) {
            GL11.glNewList(l + pass, GL11.GL_COMPILE);
            {
                if (pass == 0) {
                    GL11.glDisable(GL11.GL_BLEND);
                } else {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                }

                Tessellator.instance.startDrawingQuads();
                Tessellator.instance.addTranslation(-min.x, -min.y, -min.z);
                IMovingBlock block = null;
                try {
                    for (IMovingBlock b : structure.getBlocks()) {
                        block = b;
                        if (EventHelper.fireRenderStaticEvent(b, structure.getTrajectory(), pass))
                            continue;
                        if (b.getBlock().canRenderInPass(pass))
                            if (rb.renderBlockByRenderType(b.getBlock(), b.getPosition().x, b.getPosition().y, b.getPosition().z))
                                rendered = true;
                    }
                } catch (Exception e) {
                    new RuntimeException("Error while rendering a moving block: " + block, e).printStackTrace();
                    rendered = false;
                }
                Tessellator.instance.addTranslation(min.x, min.y, min.z);
                Tessellator.instance.draw();
            }
            GL11.glEndList();
        }

        if (!rendered) {
            structure.setRenderList(-2);
            GL11.glDeleteLists(l, 2);
        }
    }

}
