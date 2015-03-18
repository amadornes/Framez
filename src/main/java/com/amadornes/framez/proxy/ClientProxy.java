package com.amadornes.framez.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.client.RenderFrame;
import com.amadornes.framez.client.RenderMotor;
import com.amadornes.framez.client.RenderMovementBlocking;
import com.amadornes.framez.client.RenderMoving;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.tile.TileMotor;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {

        MinecraftForge.EVENT_BUS.register(new IconSupplier());

        ClientRegistry.bindTileEntitySpecialRenderer(TileMoving.class, new RenderMoving());
        MinecraftForgeClient.registerItemRenderer(FramezItems.frame, new RenderFrame());

        RenderMotor motorRenderer = new RenderMotor();
        ClientRegistry.bindTileEntitySpecialRenderer(TileMotor.class, motorRenderer);
        RenderingRegistry.registerBlockHandler(motorRenderer);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(FramezBlocks.motor), motorRenderer);

        MinecraftForge.EVENT_BUS.register(new RenderMovementBlocking());
    }

    private double frame;

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public double getFrame() {

        return frame;
    }

    @Override
    public void setFrame(double frame) {

        this.frame = frame;
    }

    @Override
    public World getWorld() {

        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public boolean isGamePaused() {

        return Minecraft.getMinecraft().isGamePaused();
    }
}
