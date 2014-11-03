package com.amadornes.framez.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.MotorPlacement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderMotorPlacement {

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {

        World world = Minecraft.getMinecraft().theWorld;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null)
            return;
        if (!(item.getItem() instanceof ItemBlock) || !(Block.getBlockFromItem(item.getItem()) instanceof BlockMotor))
            return;
        if (Minecraft.getMinecraft().gameSettings.hideGUI && Minecraft.getMinecraft().currentScreen == null)
            return;

        MovingObjectPosition mop = player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4, 0);
        if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK)
            return;

        ForgeDirection faceHit = ForgeDirection.getOrientation(mop.sideHit);
        BlockCoord b = new BlockCoord(mop.blockX, mop.blockY, mop.blockZ).add(faceHit.offsetX, faceHit.offsetY, faceHit.offsetZ);
        if (!world.isAirBlock(b.x, b.y, b.z))
            return;

        GL11.glPushMatrix();
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            Vec3 playerPos = player.getPosition(event.partialTicks);
            double x = b.x - playerPos.xCoord;
            double y = b.y - playerPos.yCoord;
            double z = b.z - playerPos.zCoord;
            GL11.glTranslated(x, y, z);

            int rotation = MotorPlacement.getPlacementRotation(mop);

            if (faceHit == ForgeDirection.WEST || faceHit == ForgeDirection.UP || faceHit == ForgeDirection.SOUTH
                    || faceHit == ForgeDirection.NORTH) {
                if (rotation == 1) {
                    rotation = 3;
                } else {
                    if (rotation == 3) {
                        rotation = 1;
                    }
                }
            }
            if (faceHit == ForgeDirection.SOUTH) {
                if (rotation == 0) {
                    rotation = 2;
                } else {
                    if (rotation == 2) {
                        rotation = 0;
                    }
                }
            }
            if (faceHit == ForgeDirection.EAST)
                rotation++;
            if (faceHit == ForgeDirection.WEST)
                rotation--;

            GL11.glTranslated(0.5, 0.5, 0.5);
            switch (faceHit) {
            case UP:
                break;
            case DOWN:
                GL11.glRotated(180, 0, 0, 1);
                break;
            case EAST:
                GL11.glRotated(-90, 0, 0, 1);
                rotation += 3;
                break;
            case WEST:
                GL11.glRotated(90, 0, 0, 1);
                rotation += 1;
                break;
            case SOUTH:
                GL11.glRotated(90, 1, 0, 0);
                break;
            case NORTH:
                GL11.glRotated(-90, 1, 0, 0);
                break;
            default:
                break;
            }
            GL11.glRotated(90 * rotation, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            double c = world.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(b.x, b.y, b.z, b.x + 1, b.y + 1, b.z + 1)) ? 1 : 0.5;

            int rgb = TileMotor.getColorMultiplierForPlayer(player.getGameProfile().getName());
            double r_ = (((rgb >> 16) & 0xff) / 256D) * c;
            double g_ = (((rgb >> 8) & 0xff) / 256D) * c;
            double b_ = ((rgb & 0xff) / 256D) * c;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4d(r_ * 0.5, g_ * 0.5, b_ * 0.5, 1);

            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.vertex(0.4, 0.025, 0.25);
            RenderHelper.vertex(0.4, 0.025, 0.5);
            RenderHelper.vertex(0.6, 0.025, 0.5);
            RenderHelper.vertex(0.6, 0.025, 0.25);

            RenderHelper.vertex(0.5, 0.025, 0.5);
            RenderHelper.vertex(0.25, 0.025, 0.5);
            RenderHelper.vertex(0.5, 0.025, 0.75);
            RenderHelper.vertex(0.75, 0.025, 0.5);
            GL11.glEnd();

            GL11.glColor4d(r_, g_, b_, 1);

            double border = 0.025;

            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.vertex(0.4 + border, 0.0275, 0.25 + border);
            RenderHelper.vertex(0.4 + border, 0.0275, 0.5 + border);
            RenderHelper.vertex(0.6 - border, 0.0275, 0.5 + border);
            RenderHelper.vertex(0.6 - border, 0.0275, 0.25 + border);

            RenderHelper.vertex(0.5, 0.0275, 0.5 + border);
            RenderHelper.vertex(0.25 + (border * 2.5), 0.0275, 0.5 + border);
            RenderHelper.vertex(0.5, 0.0275, 0.75 - (border * 1.5));
            RenderHelper.vertex(0.75 - (border * 2.5), 0.0275, 0.5 + border);
            GL11.glEnd();

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }
}
