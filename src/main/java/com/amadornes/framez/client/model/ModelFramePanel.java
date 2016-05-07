package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.client.ClientProxy;
import com.amadornes.framez.frame.FrameRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

@SuppressWarnings("deprecation")
public class ModelFramePanel implements ISmartItemModel {

    private final IFrameMaterial[] materials;

    public ModelFramePanel() {

        this.materials = new IFrameMaterial[3];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
    }

    private ModelFramePanel(IFrameMaterial[] materials) {

        this.materials = materials;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing face) {

        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(AdvancedModelRextexturer
                .retexture(ClientProxy.MODEL_ITEM_CROSS,
                        getTexture(materials[1], materials[0] != null ? EnumFrameTexture.CROSS_SMALL : EnumFrameTexture.CROSS))
                .getGeneralQuads());
        quads.addAll(AdvancedModelRextexturer.retexture(ClientProxy.MODEL_ITEM_BINDING, getTexture(materials[2], EnumFrameTexture.BINDING))
                .getGeneralQuads());
        if (materials[0] != null) quads.addAll(AdvancedModelRextexturer
                .retexture(ClientProxy.MODEL_ITEM_BORDER, getTexture(materials[0], EnumFrameTexture.BORDER)).getGeneralQuads());
        return quads;
    }

    private TextureAtlasSprite getTexture(IFrameMaterial material, EnumFrameTexture texture) {

        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(material.getTexture(texture).toString());
    }

    @Override
    public boolean isAmbientOcclusion() {

        return ClientProxy.MODEL_ITEM_BORDER.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {

        return ClientProxy.MODEL_ITEM_BORDER.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {

        return ClientProxy.MODEL_ITEM_BORDER.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {

        return ClientProxy.MODEL_ITEM_BORDER.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {

        return ClientProxy.MODEL_ITEM_BORDER.getItemCameraTransforms();
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return this;
        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("border"));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("cross"));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("binding"));
        return new ModelFramePanel(materials);
    }

}
