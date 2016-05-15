package com.amadornes.trajectory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.amadornes.trajectory.CommonProxy;
import com.amadornes.trajectory.block.TileMoving;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        super.init();

        ClientRegistry.bindTileEntitySpecialRenderer(TileMoving.class, new RenderMoving());
    }

    private float frame;

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public float getFrame() {

        return frame;
    }

    @Override
    public void setFrame(float frame) {

        this.frame = frame;
    }

    @Override
    public boolean isGamePaused() {

        return Minecraft.getMinecraft().isGamePaused();
    }

    @Override
    public World getWorld() {

        return Minecraft.getMinecraft().theWorld;
    }

}
