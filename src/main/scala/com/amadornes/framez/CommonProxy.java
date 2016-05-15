package com.amadornes.framez;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.event.EventHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {

    public void init() {

        EventHandler eh = new EventHandler();
        MinecraftForge.EVENT_BUS.register(eh);
        FMLCommonHandler.instance().bus().register(eh);
    }

    public void registerRenderers() {

    }

    public EntityPlayer getPlayer() {

        return null;
    }

    public double getFrame() {

        return 0;
    }

    public void setFrame(double frame) {

    }

    public boolean isGamePaused() {

        return false;
    }

    public World getWorld() {

        return null;
    }

    public boolean isShiftDown() {

        return false;
    }

    public boolean isCtrlDown() {

        return false;
    }

    public boolean isAltDown() {

        return false;
    }

}
