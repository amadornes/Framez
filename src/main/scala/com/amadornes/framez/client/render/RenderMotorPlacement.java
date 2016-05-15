package com.amadornes.framez.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.util.MotorPlacement;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderMotorPlacement {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {

        EntityPlayer player = event.player;
        World world = player.worldObj;
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null)
            return;
        if (!(item.getItem() instanceof ItemBlock) || !(Block.getBlockFromItem(item.getItem()) instanceof BlockMotor))
            return;

        MovingObjectPosition mop = event.target;
        if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK)
            return;

        int faceHit = mop.sideHit;
        BlockCoord b = new BlockCoord(mop.blockX, mop.blockY, mop.blockZ).offset(faceHit);
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

            if (item.getItemDamage() == 0) {
                int rotation = MotorPlacement.getPlacementRotation(mop.sideHit, (float) mop.hitVec.xCoord - mop.blockX,
                        (float) mop.hitVec.yCoord - mop.blockY, (float) mop.hitVec.zCoord - mop.blockZ);

                if (faceHit == 4 || faceHit == 1 || faceHit == 3 || faceHit == 2) {
                    if (rotation == 1) {
                        rotation = 3;
                    } else {
                        if (rotation == 3) {
                            rotation = 1;
                        }
                    }
                }
                if (faceHit == 3) {
                    if (rotation == 0) {
                        rotation = 2;
                    } else {
                        if (rotation == 2) {
                            rotation = 0;
                        }
                    }
                }
                if (faceHit == 5)
                    rotation++;
                if (faceHit == 4)
                    rotation--;

                GL11.glTranslated(0.5, 0.5, 0.5);
                switch (faceHit) {
                case 0:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case 1:
                    break;
                case 2:
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case 3:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case 4:
                    GL11.glRotated(90, 0, 0, 1);
                    rotation += 1;
                    break;
                case 5:
                    GL11.glRotated(-90, 0, 0, 1);
                    rotation += 3;
                    break;
                default:
                    break;
                }
                GL11.glRotated(90 * rotation, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                float c = world.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(b.x, b.y, b.z, b.x + 1, b.y + 1, b.z + 1)) ? 1 : 0.5F;

                int rgb = 0xDD0000;
                float r_ = (((rgb >> 16) & 0xff) / 255F) * c;
                float g_ = (((rgb >> 8) & 0xff) / 255F) * c;
                float b_ = (((rgb >> 0) & 0xff) / 255F) * c;

                Tessellator t = Tessellator.instance;

                double border = 0.025;

                t.startDrawingQuads();

                t.setColorOpaque_F(r_ * 0.5F, g_ * 0.5F, b_ * 0.5F);
                t.addVertex(0.4, 0.025, 0.25);
                t.addVertex(0.4, 0.025, 0.5);
                t.addVertex(0.6, 0.025, 0.5);
                t.addVertex(0.6, 0.025, 0.25);

                t.addVertex(0.5, 0.025, 0.5);
                t.addVertex(0.25, 0.025, 0.5);
                t.addVertex(0.5, 0.025, 0.75);
                t.addVertex(0.75, 0.025, 0.5);

                t.setColorOpaque_F(r_, g_, b_);
                t.addVertex(0.4 + border, 0.0275, 0.25 + border);
                t.addVertex(0.4 + border, 0.0275, 0.5 + border);
                t.addVertex(0.6 - border, 0.0275, 0.5 + border);
                t.addVertex(0.6 - border, 0.0275, 0.25 + border);

                t.addVertex(0.5, 0.0275, 0.5 + border);
                t.addVertex(0.25 + (border * 2.5), 0.0275, 0.5 + border);
                t.addVertex(0.5, 0.0275, 0.75 - (border * 1.5));
                t.addVertex(0.75 - (border * 2.5), 0.0275, 0.5 + border);

                t.draw();
            } else {

                GL11.glTranslated(0.5, 0.5, 0.5);
                switch (faceHit) {
                case 0:
                    GL11.glRotated(180, 0, 0, 1);
                    break;
                case 1:
                    break;
                case 2:
                    GL11.glRotated(-90, 1, 0, 0);
                    break;
                case 3:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case 4:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                case 5:
                    GL11.glRotated(-90, 0, 0, 1);
                    break;
                default:
                    break;
                }
                GL11.glTranslated(-0.5, -0.5, -0.5);

                float c = world.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(b.x, b.y, b.z, b.x + 1, b.y + 1, b.z + 1)) ? 1 : 0.5F;

                int rgb = 0xDD0000;
                float r_ = (((rgb >> 16) & 0xff) / 255F) * c;
                float g_ = (((rgb >> 8) & 0xff) / 255F) * c;
                float b_ = (((rgb >> 0) & 0xff) / 255F) * c;

                double d = 0.35;
                double border = 0.025;

                Tessellator t = Tessellator.instance;

                t.startDrawingQuads();

                t.setColorOpaque_F(r_ * 0.5F, g_ * 0.5F, b_ * 0.5F);
                t.addVertex(d, 0.025, d);
                t.addVertex(d, 0.025, 1 - d);
                t.addVertex(1 - d, 0.025, 1 - d);
                t.addVertex(1 - d, 0.025, d);

                t.setColorOpaque_F(r_, g_, b_);
                t.addVertex(d + border, 0.0275, d + border);
                t.addVertex(d + border, 0.0275, 1 - d - border);
                t.addVertex(1 - d - border, 0.0275, 1 - d - border);
                t.addVertex(1 - d - border, 0.0275, d + border);

                t.draw();
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }
}