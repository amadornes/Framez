package com.amadornes.framez.client;

import java.io.IOException;
import java.util.function.IntFunction;

import org.lwjgl.input.Keyboard;

import com.amadornes.framez.CommonProxy;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.model.ModelFrame;
import com.amadornes.framez.client.model.ModelFramePanel;
import com.amadornes.framez.client.model.ModelWrapperCamouflage;
import com.amadornes.framez.client.render.FTESRMotor;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

    public static IBakedModel MODEL_FRAME_BORDER, MODEL_FRAME_CROSS, MODEL_FRAME_ORIGINAL;

    @Override
    public void preInit() {

        super.preInit();
        ClientRegistry.bindTileEntitySpecialRenderer(TileMotor.class, new FTESRMotor());
    }

    @Override
    public void init() {

        super.init();
    }

    @Override
    public void postInit() {

        super.postInit();
    }

    @Override
    public void registerItemRenderer(Item item, String name, IntFunction<String> i2s, int variant) {

        ModelLoader.setCustomModelResourceLocation(item, variant == -1 ? 0 : variant,
                new ModelResourceLocation(ModInfo.MODID + ":" + name, "inventory" + (variant == -1 ? "" : i2s.apply(variant))));
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {

        ModelResourceLocation res = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "motor"), "normal");
        event.modelRegistry.putObject(res, new ModelWrapperCamouflage(event.modelRegistry.getObject(res)));

        event.modelRegistry.putObject(new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "multipart"),
                new ModelFrame());
        res = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "inventory");
        MODEL_FRAME_ORIGINAL = event.modelRegistry.getObject(res);
        event.modelRegistry.putObject(res, new ModelFrame());

        res = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame_panel"), "inventory0");
        event.modelRegistry.putObject(res, new ModelFramePanel(event.modelRegistry.getObject(res)));
        res = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame_panel"), "inventory1");
        event.modelRegistry.putObject(res, new ModelFramePanel(event.modelRegistry.getObject(res)));

        try {
            MODEL_FRAME_BORDER = event.modelLoader.getModel(new ResourceLocation(ModInfo.MODID, "block/frame_border")).bake(
                    TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                    r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:stone"));
            MODEL_FRAME_CROSS = event.modelLoader.getModel(new ResourceLocation(ModInfo.MODID, "block/frame_cross")).bake(
                    TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                    r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:stone"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        for (IFrameMaterial mat : FrameRegistry.INSTANCE.materials.values())
            for (EnumFrameTexture tex : EnumFrameTexture.VALUES)
                if (mat.canBeUsedAs(tex.getPart())) event.map.registerSprite(mat.getTexture(tex));
    }

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
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
