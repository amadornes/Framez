package com.amadornes.framez.client;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.amadornes.framez.CommonProxy;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.model.ModelFrame;
import com.amadornes.framez.client.model.ModelFramePanel;
import com.amadornes.framez.client.model.ModelWrapperCamouflage;
import com.amadornes.framez.client.render.FTESRMotor;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.tile.TileMotor;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
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
    public static IBakedModel MODEL_ITEM_BORDER, MODEL_ITEM_CROSS, MODEL_ITEM_BINDING;
    public static int BLOCK_TEXTURE_WIDTH, BLOCK_TEXTURE_HEIGHT;

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

        for (IBlockState state : FramezBlocks.motor.getBlockState().getValidStates()) {
            ModelResourceLocation res = getModelResourceLocation(state);
            event.modelRegistry.putObject(res, new ModelWrapperCamouflage(event.modelRegistry.getObject(res)));
        }

        // Dynamic frame part model
        {
            event.modelRegistry.putObject(new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "multipart"),
                    new ModelFrame());
            ModelResourceLocation modelLoc = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "inventory");
            MODEL_FRAME_ORIGINAL = event.modelRegistry.getObject(modelLoc);
            event.modelRegistry.putObject(modelLoc, new ModelFrame());
        }

        // Dynamic frame panel items
        {
            ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "frame_panel");
            MODEL_ITEM_BINDING = event.modelRegistry.getObject(new ModelResourceLocation(resLoc, "inventory0"));
            MODEL_ITEM_CROSS = event.modelRegistry.getObject(new ModelResourceLocation(resLoc, "inventory1"));
            MODEL_ITEM_BORDER = event.modelRegistry.getObject(new ModelResourceLocation(resLoc, "inventory2"));
            event.modelRegistry.putObject(new ModelResourceLocation(resLoc, "inventory0"), new ModelFramePanel());
            event.modelRegistry.putObject(new ModelResourceLocation(resLoc, "inventory1"), new ModelFramePanel());
        }

        // Dynamic frame block models
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
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {

        for (IFrameMaterial mat : FrameRegistry.INSTANCE.materials.values())
            for (EnumFrameTexture tex : EnumFrameTexture.VALUES)
                if (mat.canBeUsedAs(tex.getPart())) event.map.registerSprite(mat.getTexture(tex));
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        BLOCK_TEXTURE_WIDTH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        BLOCK_TEXTURE_HEIGHT = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
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

    @SuppressWarnings("rawtypes")
    private ModelResourceLocation getModelResourceLocation(IBlockState state) {

        return new ModelResourceLocation(Block.blockRegistry.getNameForObject(state.getBlock()).toString(),
                getPropertyString(Maps.<IProperty, Comparable> newLinkedHashMap(state.getProperties())));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getPropertyString(Map<IProperty, Comparable> p_178131_1_) {

        StringBuilder stringbuilder = new StringBuilder();

        for (Entry<IProperty, Comparable> entry : p_178131_1_.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty iproperty = entry.getKey();
            Comparable comparable = entry.getValue();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(iproperty.getName(comparable));
        }

        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

}
