package com.amadornes.framez.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.IMotorProvider;
import com.amadornes.framez.client.IconProvider;
import com.amadornes.framez.client.render.RenderFrame;
import com.amadornes.framez.client.render.RenderMotor;
import com.amadornes.framez.client.render.RenderMoving;
import com.amadornes.framez.compat.CompatibilityUtils;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.tile.TileMoving;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

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
            Block b = GameRegistry.findBlock(ModInfo.MODID, References.MOTOR_NAME + "." + m.getId());
            Item i = Item.getItemFromBlock(b);
            MinecraftForgeClient.registerItemRenderer(i, render);
            ClientRegistry.bindTileEntitySpecialRenderer(m.getTileClass(), render);
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileMoving.class, new RenderMoving());

        MinecraftForgeClient.registerItemRenderer(FramezItems.item_frame_part, new RenderFrame());
    }

}
