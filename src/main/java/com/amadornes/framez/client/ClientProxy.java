package com.amadornes.framez.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.amadornes.framez.CommonProxy;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.gui.GuiWrench;
import com.amadornes.framez.client.model.ModelFrame;
import com.amadornes.framez.client.model.ModelFramePanel;
import com.amadornes.framez.client.model.ModelMotorCamouflageHandler;
import com.amadornes.framez.client.model.ModelMotorDecorationHandler;
import com.amadornes.framez.client.model.ModelMotorRotationHandler;
import com.amadornes.framez.client.render.FTESRMotor;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.tile.TileMotor;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    public static IBakedModel MODEL_FRAME_BORDER, MODEL_FRAME_CROSS_IN, MODEL_FRAME_CROSS_OUT, MODEL_FRAME_ORIGINAL;
    public static IBakedModel MODEL_ITEM_BORDER, MODEL_ITEM_CROSS, MODEL_ITEM_BINDING;
    public static int BLOCK_TEXTURE_WIDTH, BLOCK_TEXTURE_HEIGHT;
    public static TextureAtlasSprite TEXTURE_TRANSPARENT;

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

        // Wrap motor models
        for (IBlockState state : FramezBlocks.motor.getBlockState().getValidStates()) {
            if (state.getValue(BlockMotor.PROPERTY_PART_TYPE) != 0) continue;
            ModelResourceLocation res = getModelResourceLocation(state);
            IBakedModel model = event.getModelRegistry().getObject(res);
            model = new ModelMotorDecorationHandler(model, state.getValue(BlockMotor.PROPERTY_LOGIC_TYPE)); // Motor Icons
            model = new ModelMotorRotationHandler(model); // Rotation
            model = new ModelMotorCamouflageHandler(model); // Camouflaging
            event.getModelRegistry().putObject(res, model);
        }

        // Dynamic frame part model
        {
            event.getModelRegistry().putObject(new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "multipart"),
                    new ModelFrame());
            ModelResourceLocation modelLoc = new ModelResourceLocation(new ResourceLocation(ModInfo.MODID, "frame"), "inventory");
            MODEL_FRAME_ORIGINAL = event.getModelRegistry().getObject(modelLoc);
            event.getModelRegistry().putObject(modelLoc, new ModelFrame());
        }

        // Dynamic frame panel items
        {
            ResourceLocation resLoc = new ResourceLocation(ModInfo.MODID, "frame_panel");
            MODEL_ITEM_BINDING = event.getModelRegistry().getObject(new ModelResourceLocation(resLoc, "inventory0"));
            MODEL_ITEM_CROSS = event.getModelRegistry().getObject(new ModelResourceLocation(resLoc, "inventory1"));
            MODEL_ITEM_BORDER = event.getModelRegistry().getObject(new ModelResourceLocation(resLoc, "inventory2"));
            event.getModelRegistry().putObject(new ModelResourceLocation(resLoc, "inventory0"), new ModelFramePanel());
            event.getModelRegistry().putObject(new ModelResourceLocation(resLoc, "inventory1"), new ModelFramePanel());
        }

        // Dynamic frame block models
        try {
            MODEL_FRAME_BORDER = ModelLoaderRegistry.getModel(new ResourceLocation(ModInfo.MODID, "block/frame_border")).bake(
                    TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                    r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:stone"));
            MODEL_FRAME_CROSS_OUT = ModelLoaderRegistry.getModel(new ResourceLocation(ModInfo.MODID, "block/frame_cross_out")).bake(
                    TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                    r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:stone"));
            MODEL_FRAME_CROSS_IN = ModelLoaderRegistry.getModel(new ResourceLocation(ModInfo.MODID, "block/frame_cross_in")).bake(
                    TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
                    r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:stone"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {

        for (IFrameMaterial mat : FrameRegistry.INSTANCE.getMaterials().values())
            for (EnumFrameTexture tex : EnumFrameTexture.VALUES)
                if (mat.canBeUsedAs(tex.getPart())) event.getMap().registerSprite(mat.getTexture(tex));
        TEXTURE_TRANSPARENT = event.getMap().registerSprite(new ResourceLocation(ModInfo.MODID, "transparent"));
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BLOCK_TEXTURE_WIDTH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        BLOCK_TEXTURE_HEIGHT = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
    }

    private boolean wrenchGui = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        ItemStack stack = player.getHeldItemMainhand();
        if (stack == null || stack.getItem() != FramezItems.wrench) return;

        if (!wrenchGui && isAltDown() && Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiWrench());
            wrenchGui = true;
        } else if (wrenchGui && !isAltDown() && Minecraft.getMinecraft().currentScreen instanceof GuiWrench) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            wrenchGui = false;
        }
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

        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()).toString(),
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
