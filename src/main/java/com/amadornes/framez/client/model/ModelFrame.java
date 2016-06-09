package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.ClientProxy;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.part.EnumFrameSideState;
import com.amadornes.framez.part.PartFrame;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import scala.actors.threadpool.Arrays;

public class ModelFrame implements IBakedModel, IPerspectiveAwareModel {

    private final Cache<IFrameMaterial[], IBakedModel> cache;
    private final EnumFrameSideState[] states;
    private final IFrameMaterial[] materials;
    private final ItemOverrideList overrides;

    public ModelFrame() {

        this.cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
        this.states = new EnumFrameSideState[PartFrame.PROPERTIES_SIDE_STATE.length];
        this.materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.getMaterials().values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
        this.overrides = new ItemOverrideList(Collections.emptyList()) {

            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null) return originalModel;
                IFrameMaterial[] materials = new IFrameMaterial[3];
                if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("border")));
                if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("cross")));
                if (tag.hasKey("binding"))
                    materials[2] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("binding")));
                try {
                    EnumFrameSideState[] states = new EnumFrameSideState[PartFrame.PROPERTIES_SIDE_STATE.length];
                    Arrays.fill(states, EnumFrameSideState.NORMAL);
                    return cache.get(materials, () -> new ModelFrame(ModelFrame.this, states, materials));
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private ModelFrame(ModelFrame parent, EnumFrameSideState[] states, IFrameMaterial[] materials) {

        this.cache = parent.cache;
        this.states = states;
        this.materials = materials;
        this.overrides = parent.overrides;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

        EnumFrameSideState[] states = this.states;
        IFrameMaterial[] materials = this.materials;

        if (state instanceof IExtendedBlockState) {
            states = new EnumFrameSideState[PartFrame.PROPERTIES_SIDE_STATE.length];
            materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
            IExtendedBlockState s = (IExtendedBlockState) state;
            for (int i = 0; i < states.length; i++)
                states[i] = s.getValue(PartFrame.PROPERTIES_SIDE_STATE[i]);
            for (int i = 0; i < materials.length; i++)
                materials[i] = s.getValue(PartFrame.PROPERTIES_MATERIAL[i]);
        }

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        TextureAtlasSprite[] textures = new TextureAtlasSprite[6];

        for (int i = 0; i < 6; i++) {
            EnumFrameTexture tex = EnumFrameTexture.BORDER;
            if (states[i] == EnumFrameSideState.PANEL) tex = EnumFrameTexture.BORDER_PANEL;
            textures[i] = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[0].getTexture(tex).toString());
        }
        quads.addAll(AdvancedModelRextexturer.retexture(state, 0L, ClientProxy.MODEL_FRAME_BORDER, textures).getQuads(state, side, rand));

        for (int i = 0; i < 6; i++) {
            EnumFrameTexture tex = null;
            if (states[i] == EnumFrameSideState.NORMAL) tex = EnumFrameTexture.CROSS;
            else if (states[i] == EnumFrameSideState.PIPE) tex = EnumFrameTexture.CROSS_OUTER;
            if (tex != null) {
                textures[i] = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[1].getTexture(tex).toString());
            } else {
                textures[i] = ClientProxy.TEXTURE_TRANSPARENT;
            }
        }
        quads.addAll(
                AdvancedModelRextexturer.retexture(state, 0L, ClientProxy.MODEL_FRAME_CROSS_OUT, textures).getQuads(state, side, rand));
        for (int i = 0; i < 3; i++) {
            TextureAtlasSprite tas = textures[i * 2 + 0];
            textures[i * 2 + 0] = textures[i * 2 + 1];
            textures[i * 2 + 1] = tas;
        }
        quads.addAll(AdvancedModelRextexturer.retexture(state, 0L, ClientProxy.MODEL_FRAME_CROSS_IN, textures).getQuads(state, side, rand));

        for (int i = 0; i < 6; i++) {
            EnumFrameTexture tex = null;
            if (states[i] == EnumFrameSideState.NORMAL) tex = EnumFrameTexture.BINDING;
            else if (states[i] == EnumFrameSideState.PIPE) tex = EnumFrameTexture.BINDING_OUTER;
            if (tex != null) {
                textures[i] = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(materials[2].getTexture(tex).toString());
            } else {
                textures[i] = ClientProxy.TEXTURE_TRANSPARENT;
            }
        }
        quads.addAll(
                AdvancedModelRextexturer.retexture(state, 0L, ClientProxy.MODEL_FRAME_CROSS_OUT, textures).getQuads(state, side, rand));
        for (int i = 0; i < 3; i++) {
            TextureAtlasSprite tas = textures[i * 2 + 0];
            textures[i * 2 + 0] = textures[i * 2 + 1];
            textures[i * 2 + 1] = tas;
        }
        quads.addAll(AdvancedModelRextexturer.retexture(state, 0L, ClientProxy.MODEL_FRAME_CROSS_IN, textures).getQuads(state, side, rand));

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
    public ItemOverrideList getOverrides() {

        return overrides;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

        Pair<? extends IBakedModel, Matrix4f> p = ClientProxy.MODEL_FRAME_ORIGINAL instanceof IPerspectiveAwareModel
                ? ((IPerspectiveAwareModel) ClientProxy.MODEL_FRAME_ORIGINAL).handlePerspective(cameraTransformType) : null;
        if (p == null) return Pair.of(this, null);
        return Pair.of(this, p.getRight());
    }

}
