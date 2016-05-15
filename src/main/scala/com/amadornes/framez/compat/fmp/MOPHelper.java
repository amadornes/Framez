package com.amadornes.framez.compat.fmp;

import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MOPHelper {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onRenderHighlight(DrawBlockHighlightEvent event) {

        if (!(event.target instanceof ExtendedMOP)
                && event.player.worldObj.getTileEntity(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof TileMultipart)
            event.setCanceled(true);
    }

}
