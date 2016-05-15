package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.ClientProxy;
import com.amadornes.framez.frame.FrameRegistry;
import com.amadornes.framez.part.PartFrame;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelFrame implements IBakedModel {

    private final Cache<IFrameMaterial[], IBakedModel> cache;
    private final boolean[] properties;
    private final IFrameMaterial[] materials;
    private final ItemOverrideList overrides;

    public ModelFrame() {

        this.cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
        this.properties = new boolean[PartFrame.PROPERTIES_BOOL.length];
        this.materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
        this.overrides = new ItemOverrideList(Collections.emptyList()) {

            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null) return originalModel;
                IFrameMaterial[] materials = new IFrameMaterial[3];
                if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("border"));
                if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("cross"));
                if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("binding"));
                try {
                    return cache.get(materials,
                            () -> new ModelFrame(ModelFrame.this, new boolean[PartFrame.PROPERTIES_BOOL.length], materials));
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private ModelFrame(ModelFrame parent, boolean[] properties, IFrameMaterial[] materials) {

        this.cache = parent.cache;
        this.properties = properties;
        this.materials = materials;
        this.overrides = parent.overrides;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

        boolean[] properties = this.properties;
        IFrameMaterial[] materials = this.materials;

        if (state instanceof IExtendedBlockState) {
            properties = new boolean[PartFrame.PROPERTIES_BOOL.length];
            materials = new IFrameMaterial[PartFrame.PROPERTIES_MATERIAL.length];
            IExtendedBlockState s = (IExtendedBlockState) state;
            for (int i = 0; i < properties.length; i++)
                properties[i] = s.getValue(PartFrame.PROPERTIES_BOOL[i]);
            for (int i = 0; i < materials.length; i++)
                materials[i] = s.getValue(PartFrame.PROPERTIES_MATERIAL[i]);
        }

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(
                new SimpleBakedModel.Builder(state, ClientProxy.MODEL_FRAME_BORDER,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[0].getTexture(EnumFrameTexture.BORDER).toString()),
                        BlockPos.ORIGIN).makeBakedModel().getQuads(state, side, rand));
        quads.addAll(
                new SimpleBakedModel.Builder(state, ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString()),
                        BlockPos.ORIGIN).makeBakedModel().getQuads(state, side, rand));
        quads.addAll(
                new SimpleBakedModel.Builder(state, ClientProxy.MODEL_FRAME_CROSS,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[2].getTexture(EnumFrameTexture.BINDING).toString()),
                        BlockPos.ORIGIN).makeBakedModel().getQuads(state, side, rand));
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

}
