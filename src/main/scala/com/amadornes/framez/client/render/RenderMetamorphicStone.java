package com.amadornes.framez.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCRenderState;

import com.amadornes.framez.block.BlockMetamorphicStone;
import com.amadornes.framez.client.LightingNB;
import com.amadornes.framez.client.RenderHelper;
import com.amadornes.framez.init.FramezBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMetamorphicStone implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.renderStandardBlock(block, x, y, z);

        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= 3) {
            RenderHelper rh = RenderHelper.instance();

            rh.reset();
            rh.start(world, x, y, z, -1);
            rh.builder.add(new LightingNB());
            rh.renderBox(AxisAlignedBB.getBoundingBox(0.01, 0.01, 0.01, 0.99, 0.99, 0.99), meta == 3 ? Blocks.water.getIcon(0, 0)
                    : (meta == 4 ? Blocks.lava.getIcon(0, 0) : block.getIcon(0, -1)));
            rh.reset();
        }

        return true;
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

        int meta = item.getItemDamage();

        GL11.glPushMatrix();
        {
            boolean alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (type == ItemRenderType.ENTITY) {
                GL11.glTranslated(-0.5, -0.5, -0.5);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslated(0, -0.0625D, 0);
            }

            RenderHelper rh = RenderHelper.instance();

            Tessellator.instance.startDrawingQuads();
            {
                if (meta >= 3) {
                    Tessellator.instance.setBrightness(0xE000E0);

                    rh.start();
                    CCRenderState.setDynamic();
                    rh.renderBox(AxisAlignedBB.getBoundingBox(0.01, 0.01, 0.01, 0.99, 0.99, 0.99), meta == 3 ? Blocks.water.getIcon(0, 0)
                            : (meta == 4 ? Blocks.lava.getIcon(0, 0) : FramezBlocks.metamorphic_stone.getIcon(0, -1)));
                }
            }
            Tessellator.instance.draw();

            boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator.instance.startDrawingQuads();
            {
                rh.start();
                CCRenderState.setDynamic();
                rh.builder.add(new LightingNB());
                rh.renderBox(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1),
                        ((BlockMetamorphicStone) Block.getBlockFromItem(item.getItem())).getIcon(meta));
            }
            Tessellator.instance.draw();
            if (!blend)
                GL11.glDisable(GL11.GL_BLEND);

            if (!alpha)
                GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
        GL11.glPopMatrix();
    }

}
