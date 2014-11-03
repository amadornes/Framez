package com.amadornes.framez.client.render;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.api.IFrameModifierProvider;
import com.amadornes.framez.client.IconProvider;
import com.amadornes.framez.modifier.ModifierRegistry;
import com.amadornes.framez.part.PartFrame;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFrame implements IItemRenderer {

    private static int renderType = 0;

    private static PartFrame rendering;

    private static Block blockFake = new BlockStone() {

        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side) {

            return getIcon(side, 0);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIcon(int side, int meta) {

            ForgeDirection face = ForgeDirection.getOrientation(side);

            IIcon tex = null;

            switch (renderType) {
            case 0:
            case 1:
                if (renderType == 1 && (face == ForgeDirection.UP || face == ForgeDirection.DOWN))
                    return IconProvider.iconNothing;
                tex = ModifierRegistry.INST.getBorderTexture(Arrays.asList(rendering.getModifiers()), face);
                if (tex == null)
                    tex = IconProvider.iconFrameBorder;
                return tex;
            case 2:
                tex = ModifierRegistry.INST.getCrossTexture(Arrays.asList(rendering.getModifiers()), RenderFrame.face);
                if (tex == null)
                    tex = rendering.isSideBlocked(RenderFrame.face) ? IconProvider.iconFrameCrossBlocked : IconProvider.iconFrameCross;
                return tex;
            }
            return null;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int side) {

            return renderFace[side];
        };
    };

    private static boolean[] renderFace = new boolean[6];

    public static RenderBlocks rb = new RenderBlocks();

    private static boolean itemRenderer = false;

    private static ForgeDirection face = ForgeDirection.UP;

    private static final void renderStandardBlock(int x, int y, int z) {

        double sep = -0.001;

        rb.setRenderBounds(rb.renderMinX == 0 ? -sep : rb.renderMinX, rb.renderMinY == 0 ? -sep : rb.renderMinY, rb.renderMinZ == 0 ? -sep
                : rb.renderMinZ, rb.renderMaxX == 1 ? 1 + sep : rb.renderMaxX, rb.renderMaxY == 1 ? 1 + sep : rb.renderMaxY,
                        rb.renderMaxZ == 1 ? 1 + sep : rb.renderMaxZ);

        if (!itemRenderer) {
            rb.renderStandardBlock(blockFake, x, y, z);
        } else {
            Tessellator.instance.setNormal((float) -rb.renderMinX, 0, 0);
            rb.renderFaceXNeg(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.WEST.ordinal(), 0));
            Tessellator.instance.setNormal((float) rb.renderMaxX, 0, 0);
            rb.renderFaceXPos(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.EAST.ordinal(), 0));
            Tessellator.instance.setNormal(0, (float) -rb.renderMinY, 0);
            rb.renderFaceYNeg(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.DOWN.ordinal(), 0));
            Tessellator.instance.setNormal(0, (float) rb.renderMaxY, 0);
            rb.renderFaceYPos(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.UP.ordinal(), 0));
            Tessellator.instance.setNormal(0, 0, (float) -rb.renderMinZ);
            rb.renderFaceZNeg(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.NORTH.ordinal(), 0));
            Tessellator.instance.setNormal(0, 0, (float) rb.renderMaxZ);
            rb.renderFaceZPos(blockFake, x, y, z, blockFake.getIcon(ForgeDirection.SOUTH.ordinal(), 0));
        }
    }

    private static final void renderBox(PartFrame frame, int minx, int miny, int minz, int maxx, int maxy, int maxz, boolean up,
            boolean down, boolean east, boolean west, boolean south, boolean north, int x, int y, int z) {

        rb.setRenderFromInside(false);
        rb.setRenderBounds(minx / 16D, miny / 16D, minz / 16D, maxx / 16D, maxy / 16D, maxz / 16D);

        renderFace[ForgeDirection.UP.ordinal()] = up;
        renderFace[ForgeDirection.DOWN.ordinal()] = down;
        renderFace[ForgeDirection.NORTH.ordinal()] = north;
        renderFace[ForgeDirection.SOUTH.ordinal()] = south;
        renderFace[ForgeDirection.EAST.ordinal()] = east;
        renderFace[ForgeDirection.WEST.ordinal()] = west;

        renderStandardBlock(x, y, z);
    }

    public static final void render(PartFrame frame, int x, int y, int z) {

        rendering = frame;

        rb.blockAccess = Minecraft.getMinecraft().theWorld;

        // Border
        {
            renderType = 0;

            boolean renderDown = frame.getConnections()[ForgeDirection.DOWN.ordinal()] == null;
            boolean renderUp = frame.getConnections()[ForgeDirection.UP.ordinal()] == null;
            boolean renderEast = frame.getConnections()[ForgeDirection.EAST.ordinal()] == null;
            boolean renderWest = frame.getConnections()[ForgeDirection.WEST.ordinal()] == null;
            boolean renderSouth = frame.getConnections()[ForgeDirection.SOUTH.ordinal()] == null;
            boolean renderNorth = frame.getConnections()[ForgeDirection.NORTH.ordinal()] == null;

            // Bottom
            {
                // North-west
                if (frame.getRender()[0])
                    renderBox(frame, 0, 0, 0, 1, 1, 1, !frame.getRender()[16], renderDown, !frame.getRender()[1], renderWest,
                            !frame.getRender()[7], renderNorth, x, y, z);
                // North
                if (frame.getRender()[1])
                    renderBox(frame, 1, 0, 0, 15, 1, 1, true, renderDown, false, false, true, renderNorth, x, y, z);
                // North-east
                if (frame.getRender()[2])
                    renderBox(frame, 15, 0, 0, 16, 1, 1, !frame.getRender()[17], renderDown, renderEast, !frame.getRender()[1],
                            !frame.getRender()[3], renderNorth, x, y, z);
                // East
                if (frame.getRender()[3])
                    renderBox(frame, 15, 0, 1, 16, 1, 15, true, renderDown, renderEast, true, false, false, x, y, z);
                // South-east
                if (frame.getRender()[4])
                    renderBox(frame, 15, 0, 15, 16, 1, 16, !frame.getRender()[18], renderDown, renderEast, !frame.getRender()[5],
                            renderSouth, !frame.getRender()[3], x, y, z);
                // South
                if (frame.getRender()[5])
                    renderBox(frame, 1, 0, 15, 15, 1, 16, true, renderDown, false, false, renderSouth, true, x, y, z);
                // South-west
                if (frame.getRender()[6])
                    renderBox(frame, 0, 0, 15, 1, 1, 16, !frame.getRender()[19], renderDown, !frame.getRender()[5], renderWest,
                            renderSouth, !frame.getRender()[7], x, y, z);
                // West
                if (frame.getRender()[7])
                    renderBox(frame, 0, 0, 1, 1, 1, 15, true, renderDown, true, renderWest, false, false, x, y, z);
            }

            // Top
            {
                // North-west
                if (frame.getRender()[8])
                    renderBox(frame, 0, 15, 0, 1, 16, 1, renderUp, !frame.getRender()[16], !frame.getRender()[9], renderWest,
                            !frame.getRender()[15], renderNorth, x, y, z);
                // North
                if (frame.getRender()[9])
                    renderBox(frame, 1, 15, 0, 15, 16, 1, renderUp, true, false, false, true, renderNorth, x, y, z);
                // North-east
                if (frame.getRender()[10])
                    renderBox(frame, 15, 15, 0, 16, 16, 1, renderUp, !frame.getRender()[17], renderEast, !frame.getRender()[9],
                            !frame.getRender()[11], renderNorth, x, y, z);
                // East
                if (frame.getRender()[11])
                    renderBox(frame, 15, 15, 1, 16, 16, 15, renderUp, true, renderEast, true, false, false, x, y, z);
                // South-east
                if (frame.getRender()[12])
                    renderBox(frame, 15, 15, 15, 16, 16, 16, renderUp, !frame.getRender()[18], renderEast, !frame.getRender()[13],
                            renderSouth, !frame.getRender()[11], x, y, z);
                // South
                if (frame.getRender()[13])
                    renderBox(frame, 1, 15, 15, 15, 16, 16, renderUp, true, false, false, renderSouth, true, x, y, z);
                // South-west
                if (frame.getRender()[14])
                    renderBox(frame, 0, 15, 15, 1, 16, 16, renderUp, !frame.getRender()[19], !frame.getRender()[13], renderWest,
                            renderSouth, !frame.getRender()[15], x, y, z);
                // West
                if (frame.getRender()[15])
                    renderBox(frame, 0, 15, 1, 1, 16, 15, renderUp, true, true, renderWest, false, false, x, y, z);
            }

            // North-west
            if (frame.getRender()[16])
                renderBox(frame, 0, 1, 0, 1, 15, 1, false, false, true, true, true, true, x, y, z);

            // North-east
            if (frame.getRender()[17])
                renderBox(frame, 15, 1, 0, 16, 15, 1, false, false, true, true, true, true, x, y, z);

            // South-east
            if (frame.getRender()[18])
                renderBox(frame, 15, 1, 15, 16, 15, 16, false, false, true, true, true, true, x, y, z);

            // South-west
            if (frame.getRender()[19])
                renderBox(frame, 0, 1, 15, 1, 15, 16, false, false, true, true, true, true, x, y, z);
        }

        // Crosses
        if (true) {
            renderType = 2;

            double sep = 0.001;
            double depth = 1 / 32D;

            rb.setRenderFromInside(false);

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.UP.ordinal()] = true;
            renderFace[ForgeDirection.DOWN.ordinal()] = true;

            rb.setRenderBounds(0 + (frame.getConnection(ForgeDirection.WEST) == null ? sep : 0), 1 - depth,
                    0 + (frame.getConnection(ForgeDirection.NORTH) == null ? sep : 0),
                    1 - (frame.getConnection(ForgeDirection.EAST) == null ? sep : 0), 1 - depth,
                    1 - (frame.getConnection(ForgeDirection.SOUTH) == null ? sep : 0));
            if (frame.getConnection(ForgeDirection.UP) == null || frame.isSideBlocked(ForgeDirection.UP)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.UP;
                renderStandardBlock(x, y, z);
            }
            rb.setRenderBounds(0 + sep, 0 + depth, 0 + sep, 1 - sep, 0 + depth, 1 - sep);
            if (frame.getConnection(ForgeDirection.DOWN) == null || frame.isSideBlocked(ForgeDirection.DOWN)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.DOWN;
                renderStandardBlock(x, y, z);
            }

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.EAST.ordinal()] = true;
            renderFace[ForgeDirection.WEST.ordinal()] = true;

            rb.setRenderBounds(1 - depth, 0 + sep, 0 + sep, 1 - depth, 1 - sep, 1 - sep);
            if (frame.getConnection(ForgeDirection.EAST) == null || frame.isSideBlocked(ForgeDirection.EAST)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.EAST;
                renderStandardBlock(x, y, z);
            }
            rb.setRenderBounds(0 + depth, 0 + (frame.getConnection(ForgeDirection.DOWN) == null ? sep : 0), 0 + (frame
                    .getConnection(ForgeDirection.NORTH) == null ? sep : 0), 0 + depth,
                    1 - (frame.getConnection(ForgeDirection.UP) == null ? sep : 0),
                    1 - (frame.getConnection(ForgeDirection.SOUTH) == null ? sep : 0));
            if (frame.getConnection(ForgeDirection.WEST) == null || frame.isSideBlocked(ForgeDirection.WEST)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.WEST;
                renderStandardBlock(x, y, z);
            }

            Arrays.fill(renderFace, false);
            renderFace[ForgeDirection.SOUTH.ordinal()] = true;
            renderFace[ForgeDirection.NORTH.ordinal()] = true;

            rb.setRenderBounds(0 + (frame.getConnection(ForgeDirection.WEST) == null ? sep : 0), 0 + (frame
                    .getConnection(ForgeDirection.DOWN) == null ? sep : 0), 1 - depth,
                    1 - (frame.getConnection(ForgeDirection.EAST) == null ? sep : 0),
                    1 - (frame.getConnection(ForgeDirection.UP) == null ? sep : 0), 1 - depth);
            if (frame.getConnection(ForgeDirection.SOUTH) == null || frame.isSideBlocked(ForgeDirection.SOUTH)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.SOUTH;
                renderStandardBlock(x, y, z);
            }
            rb.setRenderBounds(0 + sep, 0 + sep, 0 + depth, 1 - sep, 1 - sep, 0 + depth);
            if (frame.getConnection(ForgeDirection.NORTH) == null || frame.isSideBlocked(ForgeDirection.NORTH)
                    || !frame.hasModifier(References.Modifiers.CONNECTED)) {
                face = ForgeDirection.NORTH;
                renderStandardBlock(x, y, z);
            }
        }

        itemRenderer = false;
    }

    public static final void renderItem(ItemStack item) {

        PartFrame f = new PartFrame();
        for (IFrameModifierProvider p : ModifierRegistry.INST.getModifiers(item))
            f.addModifier(p.instantiate(f));

        Arrays.fill(f.getRender(), true);

        itemRenderer = true;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator.instance.startDrawingQuads();
        render(f, 0, 0, 0);
        Tessellator.instance.draw();

        GL11.glDisable(GL11.GL_BLEND);
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

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                GL11.glScaled(0.5, 0.5, 0.5);
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

            renderItem(item);
        }
        GL11.glPopMatrix();
    }

}
