package com.amadornes.framez.client.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.MotorCache;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

public class RenderMovementBlocking {

    private double opacity = 0;
    private double speed = 0.01;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        if (event.side != Side.CLIENT || event.phase == Phase.END)
            return;

        speed = 0.05;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null)
            return;
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null || FramezApi.instance().getWrench(item) == null) {
            opacity -= speed;
        } else {
            opacity += speed;
        }
        opacity = Math.max(0, Math.min(opacity, 1));
    }

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {

        if (opacity == 0)
            return;

        World world = Minecraft.getMinecraft().theWorld;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        MovingObjectPosition mop = world.rayTraceBlocks(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (Minecraft.getMinecraft().gameSettings.hideGUI && Minecraft.getMinecraft().currentScreen == null)
            return;

        double thickness = 1 / 32D;

        GL11.glPushMatrix();
        {
            Vec3 playerPos = player.getPosition(event.partialTicks);
            GL11.glTranslated(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glColor4d(1, 1, 1, opacity);

            List<TileMotor> l = null;

            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                TileEntity te = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                if (te != null && te instanceof TileMotor) {
                    l = Arrays.asList((TileMotor) te);
                }
            }

            if (l == null)
                l = new ArrayList<TileMotor>(MotorCache.getLoadedMotors());

            for (TileMotor m : l) {
                if (m == null)
                    break;
                if (m.getWorld() == world) {
                    Set<MovementIssue> issues = m.getMovementIssues();
                    if (issues == null || issues.size() == 0)
                        continue;

                    int j = 0;
                    for (MovementIssue i : issues) {
                        j++;

                        AxisAlignedBB aabb = i.getAABB().copy();
                        if (i.getFace() > 0 && i.getFace() < 6) {
                            aabb = new Cuboid6(aabb).apply(Rotation.sideRotations[i.getFace()].at(Vector3.center)).toAABB();
                            aabb = aabb.expand(thickness * (0.5 + Math.abs(BlockCoord.sideOffsets[i.getFace()].x) * 0.5 + j * 0.001),
                                    thickness * (0.5 + Math.abs(BlockCoord.sideOffsets[i.getFace()].y) * 0.5 + j * 0.001), thickness
                                            * (0.5 + Math.abs(BlockCoord.sideOffsets[i.getFace()].z) * 0.5 + j * 0.001));
                        } else {
                            aabb = aabb.expand(thickness, thickness, thickness);
                        }
                        aabb.offset(i.getPosition().x, i.getPosition().y, i.getPosition().z);
                        Color color = new Color(i.getColor());

                        {
                            GL11.glColor4d(color.getRed() / 255D, color.getGreen() / 255D, color.getBlue() / 255D, .25 * opacity);
                            GL11.glBegin(GL11.GL_QUADS);
                            drawBox(aabb);
                            GL11.glEnd();
                        }

                        {
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glColor4d(0.75 * color.getRed() / 255D, 0.75 * color.getGreen() / 255D, 0.75 * color.getBlue() / 255D,
                                    .5 * opacity);
                            GL11.glLineWidth(2);
                            GL11.glBegin(GL11.GL_LINES);
                            GL11.glVertex3d(m.getX() + 0.5, m.getY() + 0.5, m.getZ() + 0.5);
                            GL11.glVertex3d((aabb.minX + aabb.maxX) / 2D, (aabb.minY + aabb.maxY) / 2D, (aabb.minZ + aabb.maxZ) / 2D);
                            GL11.glEnd();
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                        }
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }

    public static void drawBox(AxisAlignedBB box) {

        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);

        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);

        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);

        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);

        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);

        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
    }
}
