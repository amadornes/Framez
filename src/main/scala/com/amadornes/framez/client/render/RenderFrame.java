package com.amadornes.framez.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import codechicken.lib.render.CCRenderState;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.modifier.IFrameSideModifier;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IFrame.IFrameBlock;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.framez.api.movement.IModifiableFrame;
import com.amadornes.framez.api.wrench.IFramePart;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderFrame extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int ID = RenderingRegistry.getNextAvailableRenderId();
    public static int pass = 0;

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float frame) {

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        IFrameRenderData renderData = block instanceof IFrameBlock ? FramezApi.instance().wrapFrameBlockRenderData(world,
                new BlockPos(x, y, z), (IFrameBlock) block) : null;

        if (renderData == null) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te == null || !(te instanceof IFrame))
                return false;
            renderData = (IFrame) te;
        }

        RenderHelper helper = RenderHelper.instance();
        BlockPos position = new BlockPos(x, y, z);

        helper.reset();
        helper.start(world, x, y, z, pass);
        helper.setOverrideTexture(renderer.overrideBlockTexture);
        boolean result = renderFrame(world, position, renderData, pass);
        helper.setOverrideTexture(null);
        helper.reset();

        return result;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return ID;
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

        glPushMatrix();
        {
            boolean blend = glGetBoolean(GL_BLEND);
            boolean alpha = glGetBoolean(GL_ALPHA_TEST);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_ALPHA_TEST);

            if (type == ItemRenderType.INVENTORY)
                glTranslated(0, -0.1, 0);
            else if (type == ItemRenderType.ENTITY)
                glTranslated(-0.5, -0.5, -0.5);

            List<IFramePart> frameParts = FramezUtils.loadSilkyData(null, null, FramezUtils.placementFrame, item);

            RenderHelper.instance().start();
            CCRenderState.setDynamic();
            Tessellator.instance.startDrawingQuads();
            renderFrame(null, null, FramezUtils.placementFrame, 0);
            renderFrame(null, null, FramezUtils.placementFrame, 1);
            Tessellator.instance.draw();
            RenderHelper.instance().reset();

            for (IFramePart part : frameParts)
                part.renderItem(type);

            if (!blend)
                glDisable(GL_BLEND);
            if (!alpha)
                glDisable(GL_ALPHA_TEST);
        }
        glPopMatrix();
    }

    public static boolean renderFrame(IBlockAccess world, BlockPos position, IFrameRenderData data, int pass) {

        boolean result = RenderHelper.instance().renderFrame(world, position, data.getMaterial(), data, pass);
        if (data instanceof IModifiableFrame) {
            IModifiableFrame frame = (IModifiableFrame) data;
            for (int s = 0; s < 6; s++)
                for (IFrameSideModifier mod : frame.getSideModifiers(s))
                    mod.renderStatic(world, position, frame, s, pass);
            // TODO: Render dynamic if item
        }
        return result;
    }
}
