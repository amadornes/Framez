package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.ClientProxy;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.part.PartFrame;

import mcmultipart.client.multipart.ISmartMultipartModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class ModelFrame implements ISmartMultipartModel, ISmartItemModel, IFlexibleBakedModel {

    private final boolean[] properties;
    private final IFrameMaterial[] materials;

    public ModelFrame() {

        properties = new boolean[PartFrame.PROPERTIES_BOOL.length];
        materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
    }

    private ModelFrame(boolean[] properties, IFrameMaterial[] materials) {

        this.properties = properties;
        this.materials = materials;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing face) {

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_BORDER,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[0].getTexture(EnumFrameTexture.BORDER).toString())).makeBakedModel()
                                        .getFaceQuads(face));
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString())).makeBakedModel()
                                        .getFaceQuads(face));
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[2].getTexture(EnumFrameTexture.BINDING).toString())).makeBakedModel()
                                        .getFaceQuads(face));
        return quads;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_BORDER,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[0].getTexture(EnumFrameTexture.BORDER).toString())).makeBakedModel()
                                        .getGeneralQuads());
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString())).makeBakedModel()
                                        .getGeneralQuads());
        quads.addAll(
                new SimpleBakedModel.Builder(ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[2].getTexture(EnumFrameTexture.BINDING).toString())).makeBakedModel()
                                        .getGeneralQuads());
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {

        return true;
    }

    @Override
    public boolean isGui3d() {

        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {

        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {

        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString());
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {

        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public VertexFormat getFormat() {

        return ((IFlexibleBakedModel) ClientProxy.MODEL_FRAME_ORIGINAL).getFormat();
    }

    @Override
    public IBakedModel handlePartState(IBlockState state) {

        IExtendedBlockState s = (IExtendedBlockState) state;
        boolean[] properties = new boolean[PartFrame.PROPERTIES_BOOL.length];
        IFrameMaterial[] materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
        for (int i = 0; i < properties.length; i++)
            properties[i] = s.getValue(PartFrame.PROPERTIES_BOOL[i]);
        for (int i = 0; i < materials.length; i++)
            materials[i] = s.getValue(PartFrame.PROPERTIES_MATERIAL[i]);
        return new ModelFrame(properties, materials);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return this;
        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("border"));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("cross"));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("binding"));
        return new ModelFrame(properties, materials);
    }

}
