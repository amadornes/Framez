package com.amadornes.framez.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.amadornes.framez.CommonProxy;
import com.amadornes.framez.block.BlockFrame;
import com.amadornes.framez.client.render.RenderFrame;
import com.amadornes.framez.client.render.RenderMetamorphicStone;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.client.render.RenderMotorCore;
import com.amadornes.framez.client.render.RenderMotorPlacement;
import com.amadornes.framez.client.render.RenderMovementBlocking;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.tile.TileFrame;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        super.init();

        MinecraftForge.EVENT_BUS.register(new IconSupplier());

        ClientEventHandler eh = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eh);
        FMLCommonHandler.instance().bus().register(eh);

        MinecraftForge.EVENT_BUS.register(new RenderMotorPlacement());
    }

    @Override
    public void registerRenderers() {

        RenderMetamorphicStone metamorphicRender = new RenderMetamorphicStone();
        RenderingRegistry.registerBlockHandler(metamorphicRender);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(FramezBlocks.metamorphic_stone), metamorphicRender);

        RenderFrame frameRenderer = new RenderFrame();
        RenderingRegistry.registerBlockHandler(frameRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileFrame.class, frameRenderer);
        for (BlockFrame m : FramezBlocks.frames.values())
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(m), frameRenderer);

        RenderMotor motorRenderer = new RenderMotor();
        RenderingRegistry.registerBlockHandler(motorRenderer);
        for (Block m : FramezBlocks.motors.values())
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(m), motorRenderer);

        MinecraftForgeClient.registerItemRenderer(FramezItems.motor_core, new RenderMotorCore());

        RenderMovementBlocking rmb = new RenderMovementBlocking();
        MinecraftForge.EVENT_BUS.register(rmb);
        FMLCommonHandler.instance().bus().register(rmb);
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

    @Override
    public boolean isShiftDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    @Override
    public boolean isCtrlDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    @Override
    public boolean isAltDown() {

        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}
