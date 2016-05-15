package com.amadornes.framez.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.MouseEvent;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.movement.MovementRegistry;
import com.amadornes.framez.network.NetworkHandler;
import com.amadornes.framez.network.packet.PacketWrenchMode;
import com.amadornes.trajectory.api.vec.BlockPos;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientEventHandler {

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {

        // We only want to process wheel events
        if (event.button < 0) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            if (Framez.proxy.isCtrlDown()) {
                ItemStack itemStack = entityPlayer.getHeldItem();
                if (itemStack != null && itemStack.getItem() == FramezItems.wrench) {
                    if (event.dwheel != 0)
                        NetworkHandler.instance().sendToServer(new PacketWrenchMode(entityPlayer.inventory.currentItem, event.dwheel < 0));
                    event.setCanceled(true);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderHighlight(DrawBlockHighlightEvent event) {

        World world = event.player.worldObj;
        BlockPos position = new BlockPos(event.target.blockX, event.target.blockY, event.target.blockZ);

        if (!(world.getBlock(position.x, position.y, position.z) instanceof BlockFrame))
            return;

        renderHighlight(world, position, MovementRegistry.instance.getFrameAt(world, position), event);
        event.setCanceled(true);
    }

    public static void renderHighlight(World world, BlockPos position, IFrame frame, DrawBlockHighlightEvent event) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0, 0, 0, 0.4F);
        GL11.glLineWidth(2);
        GL11.glDepthMask(true);
        GL11.glPushMatrix();

        double px = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks;
        double py = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks;
        double pz = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks;
        GL11.glTranslated(position.x - px, position.y - py, position.z - pz);
        {
            double d = frame.canHaveCovers() ? 0.005 : 0.001;
            double din = 0.001;

            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, frame.isSideHidden(0) ? d : -d, 2 / 16D + din, 14 / 16D - din,
                    1 + (frame.isSideHidden(1) ? -d : d), 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(frame.isSideHidden(4) ? d : -d, 2 / 16D + din, 2 / 16D + din, 1 + (frame
                    .isSideHidden(5) ? -d : d), 14 / 16D - din, 14 / 16D - din));
            RenderUtils.drawCuboidOutline(new Cuboid6(2 / 16D + din, 2 / 16D + din, frame.isSideHidden(2) ? d : -d, 14 / 16D - din,
                    14 / 16D - din, 1 + (frame.isSideHidden(3) ? -d : d)));

            RenderUtils.drawCuboidOutline(new Cuboid6(0, 0, 0, 1, 1, 1).expand(d * 1.5));
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
