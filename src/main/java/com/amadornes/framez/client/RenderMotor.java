package com.amadornes.framez.client;

import java.awt.Rectangle;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.texture.Layout;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementSlide;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMotor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    // Item rendering

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        Tessellator t = Tessellator.instance;

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.INVENTORY)
                GL11.glTranslated(0, -0.0625, 0);
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(-0.5, -0.45, -0.5);

            t.startDrawingQuads();

            RenderHelper renderer = RenderHelper.instance;
            renderer.fullReset();

            IIcon border = IconSupplier.motor_border;
            IIcon center = IconSupplier.motor_center;

            renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), border);

            renderer.setRenderFromInside(true);
            {
                renderer.renderBox(new Vec3dCube(0, 2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D), border);
                renderer.renderBox(new Vec3dCube(2 / 16D, 0, 2 / 16D, 14 / 16D, 1, 14 / 16D), border);
                renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1), border);
            }
            renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), center);
            renderer.setRenderFromInside(false);
            renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), center);

            t.draw();
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    // Static rendering

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rb) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileMotor))
            return false;
        TileMotor te = (TileMotor) tile;

        RenderHelper renderer = RenderHelper.instance;
        renderer.fullReset();
        renderer.setRenderCoords(world, x, y, z);

        IIcon border = IconSupplier.motor_border;
        IIcon center = IconSupplier.motor_center;

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), border);

        renderer.setRenderFromInside(true);
        {
            renderer.renderBox(new Vec3dCube(0, 2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D), border);
            renderer.renderBox(new Vec3dCube(2 / 16D, 0, 2 / 16D, 14 / 16D, 1, 14 / 16D), border);
            renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1), border);
        }
        AxisAlignedBB box = new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D).toAABB();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
            if (world.getBlock(x * dir.offsetX, y * dir.offsetY, z * dir.offsetZ).canConnectRedstone(world, x, y, z,
                    Direction.getMovementDirection(dir.offsetX, dir.offsetZ)))
                box = box.addCoord(dir.offsetX * (1 / 16D - 0.001), dir.offsetY * (1 / 16D - 0.001), dir.offsetZ * (1 / 16D - 0.001));

        renderer.renderBox(new Vec3dCube(box), center);
        renderer.setRenderFromInside(false);
        renderer.renderBox(new Vec3dCube(box), center);

        // Render arrows
        {
            renderer.setColor(0xFF0000);

            IMovement movement = te.getMovement();

            if (movement != null) {
                if (movement instanceof IMovementSlide) {
                    ForgeDirection face = te.getFace();
                    ForgeDirection mdir = ((IMovementSlide) movement).getDirection();
                    ForgeDirection dir = null;
                    int rot = 0;
                    boolean flip = false;
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        if (d != face && d != face.getOpposite() && d != mdir && d != mdir.getOpposite()) {
                            dir = d;
                            if (d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH) {
                                if (face == ForgeDirection.DOWN && mdir == ForgeDirection.EAST)
                                    flip = true;
                                if (face == ForgeDirection.UP) {
                                    rot = 2;
                                    if (mdir == ForgeDirection.WEST)
                                        flip = true;
                                }
                                if (face == ForgeDirection.WEST) {
                                    rot = 3;
                                    if (mdir == ForgeDirection.DOWN)
                                        flip = true;
                                }
                                if (face == ForgeDirection.EAST) {
                                    rot = 1;
                                    if (mdir == ForgeDirection.UP)
                                        flip = true;
                                }
                            }
                            if (d == ForgeDirection.EAST || d == ForgeDirection.WEST) {
                                if (face == ForgeDirection.DOWN) {
                                    rot = 1;
                                    if (mdir == ForgeDirection.NORTH)
                                        flip = true;
                                }
                                if (face == ForgeDirection.UP) {
                                    rot = 3;
                                    if (mdir == ForgeDirection.SOUTH)
                                        flip = true;
                                }
                                if (face == ForgeDirection.NORTH) {
                                    rot = 2;
                                    if (mdir == ForgeDirection.UP)
                                        flip = true;
                                }
                                if (face == ForgeDirection.SOUTH && mdir == ForgeDirection.DOWN)
                                    flip = true;

                            }
                            if (d == ForgeDirection.DOWN || d == ForgeDirection.UP) {
                                if (face == ForgeDirection.SOUTH && mdir == ForgeDirection.EAST)
                                    flip = true;
                                if (face == ForgeDirection.NORTH) {
                                    rot = 2;
                                    if (mdir == ForgeDirection.WEST)
                                        flip = true;
                                }
                                if (face == ForgeDirection.EAST) {
                                    rot = 1;
                                    if (mdir == ForgeDirection.NORTH)
                                        flip = true;
                                }
                                if (face == ForgeDirection.WEST) {
                                    rot = 3;
                                    if (mdir == ForgeDirection.SOUTH)
                                        flip = true;
                                }
                            }
                            break;
                        }
                    }

                    Layout layout = new Layout("/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_ARROW_SLIDE);
                    double pixel = 1 / (double) layout.getLayout(0xFF0000).getWidth();
                    for (Rectangle r : layout.getSimplifiedLayout(0xFF0000).getRectangles())
                        renderer.renderBox(
                                new Vec3dCube(flip ? (1 - r.getMinX() * pixel) : r.getMinX() * pixel, 1 / 64D, r.getMinY() * pixel,
                                        flip ? (1 - r.getMaxX() * pixel) : r.getMaxX() * pixel, 63 / 64D, r.getMaxY() * pixel).rotate(0,
                                                rot * 90, 0, Vec3d.center).rotate(dir, Vec3d.center), center);
                }
            }
        }

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    // Dynamic rendering

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float frame) {

    }

}
