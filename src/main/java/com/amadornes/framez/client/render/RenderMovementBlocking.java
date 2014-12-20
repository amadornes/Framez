package com.amadornes.framez.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import codechicken.lib.vec.BlockCoord;

import com.amadornes.framez.api.IFramezWrench;
import com.amadornes.framez.movement.MotorCache;
import com.amadornes.framez.tile.TileMotor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderMovementBlocking {

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {

        World world = Minecraft.getMinecraft().theWorld;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
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

            for (TileMotor m : MotorCache.getLoadedMotors()) {
                if (m.getWorldObj() == world) {
                    BlockCoord b = m.getBlockingCoord();
                    if (b == null)
                        continue;

                    double x = b.x - playerPos.xCoord;
                    double y = b.y - playerPos.yCoord;
                    double z = b.z - playerPos.zCoord;

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslated(x - thickness, y - thickness, z - thickness);
                        GL11.glScaled(1 + (thickness * 2), 1 + (thickness * 2), 1 + (thickness * 2));

                        GL11.glColor4d(1, 0, 0, .25);

                        GL11.glBegin(GL11.GL_QUADS);
                        RenderHelper.drawColoredCube();
                        GL11.glEnd();
                    }
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    {
                        GL11.glColor4d(.5, 0, 0, .75);

                        GL11.glLineWidth(2);

                        GL11.glBegin(GL11.GL_LINES);
                        RenderHelper.vertex(m.xCoord - playerPos.xCoord + 0.5, m.yCoord - playerPos.yCoord + 0.5, m.zCoord
                                - playerPos.zCoord + 0.5);
                        RenderHelper.vertex(x + 0.5, y + 0.5, z + 0.5);
                        GL11.glEnd();
                    }
                    GL11.glPopMatrix();
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }
}
