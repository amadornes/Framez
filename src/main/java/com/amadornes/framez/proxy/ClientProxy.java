package com.amadornes.framez.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.client.IconProvider;
import com.amadornes.framez.client.render.RenderFrame;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.client.render.RenderMotorPlacement;
import com.amadornes.framez.client.render.RenderMoving;
import com.amadornes.framez.compat.CompatibilityUtils;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {

        MinecraftForge.EVENT_BUS.register(new IconProvider());
    }

    @Override
    public void registerRenders() {

        CompatibilityUtils.registerRenders();

        RenderMotor render = new RenderMotor();

        RenderingRegistry.registerBlockHandler(render);
        for (IMotorProvider m : FramezApi.inst().getMotorRegistry().getRegisteredMotors()) {
            Block b = GameRegistry.findBlock(ModInfo.MODID, References.Names.Registry.MOTOR + "." + m.getId());
            Item i = Item.getItemFromBlock(b);
            MinecraftForgeClient.registerItemRenderer(i, render);
            ClientRegistry.bindTileEntitySpecialRenderer(m.getTileClass(), render);
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileMoving.class, new RenderMoving());

        MinecraftForgeClient.registerItemRenderer(FramezItems.frame, new RenderFrame());

        RenderMotorPlacement renderMotorPlacement = new RenderMotorPlacement();
        FMLCommonHandler.instance().bus().register(renderMotorPlacement);
        MinecraftForge.EVENT_BUS.register(renderMotorPlacement);
    }

    @Override
    public void setPlayer(EntityPlayer player) {

        Minecraft.getMinecraft().thePlayer = (EntityClientPlayerMP) player;
    }

    @Override
    public void setWorld(World world) {

        Minecraft.getMinecraft().theWorld = (WorldClient) world;
    }

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getWorld() {

        return Minecraft.getMinecraft().theWorld;
    }

    private double frame = 0;

    @Override
    public double getFrame() {

        return frame;
    }

    @Override
    public void setFrame(double frame) {

        this.frame = frame;
    }

    @Override
    public boolean isGamePaused() {

        return Minecraft.getMinecraft().isGamePaused();
    }

}
