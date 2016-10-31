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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public class ModelFramePanel implements IBakedModel, IPerspectiveAwareModel {

    private final Cache<IFrameMaterial[], IBakedModel> cache;
    private final IFrameMaterial[] materials;
    private final ItemOverrideList overrides;
    private final ItemStack stack;

    public ModelFramePanel() {

        this.cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
        this.materials = new IFrameMaterial[3];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.getMaterials().values().iterator();
        for (int i = 0; i < materials.length; i++) {
            materials[i] = it.next();
        }
        this.overrides = new ItemOverrideList(Collections.emptyList()) {

            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {

                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null) {
                    return originalModel;
                }
                IFrameMaterial[] materials = new IFrameMaterial[3];
                if (tag.hasKey("border")) {
                    materials[0] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("border")));
                }
                if (tag.hasKey("cross")) {
                    materials[1] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("cross")));
                }
                if (tag.hasKey("binding")) {
                    materials[2] = FrameRegistry.INSTANCE.getMaterial(new ResourceLocation(tag.getString("binding")));
                }
                try {
                    return cache.get(materials, () -> new ModelFramePanel(ModelFramePanel.this, materials, stack));
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        stack = new ItemStack(Items.APPLE);
    }

    private ModelFramePanel(ModelFramePanel parent, IFrameMaterial[] materials, ItemStack stack) {

        this.cache = parent.cache;
        this.materials = materials;
        this.overrides = parent.overrides;
        this.stack = stack;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(AdvancedModelRextexturer
                .retexture(null, 0,
                        ClientProxy.MODEL_ITEM_CROSS.getOverrides().handleItemState(ClientProxy.MODEL_ITEM_CROSS, stack,
                                Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer),
                        getTexture(materials[1], materials[0] != null ? EnumFrameTexture.CROSS_SMALL : EnumFrameTexture.CROSS))
                .getQuads(state, side, rand));
        quads.addAll(AdvancedModelRextexturer.retexture(null, 0,
                ClientProxy.MODEL_ITEM_BINDING.getOverrides().handleItemState(ClientProxy.MODEL_ITEM_BINDING, stack,
                        Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer),
                getTexture(materials[2], EnumFrameTexture.BINDING)).getQuads(state, side, rand));
        if (materials[0] != null) {
            quads.addAll(AdvancedModelRextexturer.retexture(null, 0,
                    ClientProxy.MODEL_ITEM_BORDER.getOverrides().handleItemState(ClientProxy.MODEL_ITEM_BORDER, stack,
                            Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer),
                    getTexture(materials[0], EnumFrameTexture.BORDER)).getQuads(state, side, rand));
        }
        return quads;
    }

    private TextureAtlasSprite getTexture(IFrameMaterial material, EnumFrameTexture texture) {

        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(material.getTexture(texture).toString());
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

    @SuppressWarnings("deprecation")
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {

        return ClientProxy.MODEL_ITEM_BORDER.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {

        return overrides;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

        Pair<? extends IBakedModel, Matrix4f> p = ClientProxy.MODEL_ITEM_BORDER instanceof IPerspectiveAwareModel
                ? ((IPerspectiveAwareModel) ClientProxy.MODEL_ITEM_BORDER).handlePerspective(cameraTransformType) : null;
        if (p == null) {
            return Pair.of(this, null);
        }
        return Pair.of(this, p.getRight());
    }

}
