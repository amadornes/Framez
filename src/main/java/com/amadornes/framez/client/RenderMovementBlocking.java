package com.amadornes.framez.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderUtils;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.IFramezWrench;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.util.MotorCache;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderMovementBlocking {

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {

        World world = Minecraft.getMinecraft().theWorld;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        MovingObjectPosition mop = world.rayTraceBlocks(RayTracer.instance().getStartVector(player).toVec3(), RayTracer.instance()
                .getEndVector(player).toVec3());
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null)
            return;
        if (!(item.getItem() instanceof IFramezWrench))
            return;
        if (Minecraft.getMinecraft().gameSettings.hideGUI && Minecraft.getMinecraft().currentScreen == null)
            return;

        double thickness = 1 / 32D;

        GL11.glPushMatrix();
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            Vec3 playerPos = player.getPosition(event.partialTicks);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4d(1, 1, 1, 1);

            List<TileMotor> l = null;

            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                TileEntity te = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
                if (te != null && te instanceof TileMotor) {
                    l = new ArrayList<TileMotor>();
                    l.add((TileMotor) te);
                }
            }

            if (l == null)
                l = MotorCache.getLoadedMotors();

            for (TileMotor m : l) {
                if (m.getWorldObj() == world) {
                    List<Vec3i> blocking = m.getBlocking();
                    if (blocking == null || blocking.size() == 0)
                        continue;

                    for (Vec3i b : blocking) {
                        double x = b.getX() - playerPos.xCoord;
                        double y = b.getY() - playerPos.yCoord;
                        double z = b.getZ() - playerPos.zCoord;

                        GL11.glPushMatrix();
                        {
                            GL11.glTranslated(x - thickness, y - thickness, z - thickness);
                            GL11.glScaled(1 + (thickness * 2), 1 + (thickness * 2), 1 + (thickness * 2));

                            GL11.glColor4d(1, 0, 0, .25);

                            GL11.glBegin(GL11.GL_QUADS);
                            RenderUtils.drawColoredCube();
                            GL11.glEnd();
                        }
                        GL11.glPopMatrix();

                        GL11.glPushMatrix();
                        {
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glColor4d(.75, 0, 0, .5);

                            GL11.glLineWidth(2);

                            GL11.glBegin(GL11.GL_LINES);
                            GL11.glVertex3d(m.xCoord - playerPos.xCoord + 0.5, m.yCoord - playerPos.yCoord + 0.5, m.zCoord
                                    - playerPos.zCoord + 0.5);
                            GL11.glVertex3d(x + 0.5, y + 0.5, z + 0.5);
                            GL11.glEnd();
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                        }
                        GL11.glPopMatrix();
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }
}
