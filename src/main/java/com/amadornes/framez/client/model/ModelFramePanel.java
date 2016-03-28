package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.amadornes.framez.api.frame.EnumFrameTexture;
import com.amadornes.framez.api.frame.IFrameMaterial;
import com.amadornes.framez.frame.FrameRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

@SuppressWarnings("deprecation")
public class ModelFramePanel implements ISmartItemModel {

    private final IBakedModel model;
    private final IFrameMaterial[] materials;

    public ModelFramePanel(IBakedModel model) {

        this.model = model;
        this.materials = new IFrameMaterial[3];
        Iterator<IFrameMaterial> it = FrameRegistry.INSTANCE.materials.values().iterator();
        for (int i = 0; i < materials.length; i++)
            materials[i] = it.next();
    }

    private ModelFramePanel(IBakedModel model, IFrameMaterial[] materials) {

        this.model = model;
        this.materials = materials;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing face) {

        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {

        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        quads.addAll(
                new SimpleBakedModel.Builder(model,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[1].getTexture(EnumFrameTexture.CROSS).toString())).makeBakedModel()
                                        .getGeneralQuads());
        quads.addAll(
                new SimpleBakedModel.Builder(model,
                        Minecraft.getMinecraft().getTextureMapBlocks()
                                .getAtlasSprite(materials[2].getTexture(EnumFrameTexture.BINDING).toString())).makeBakedModel()
                                        .getGeneralQuads());
        if (materials[0] != null)
            quads.addAll(new SimpleBakedModel.Builder(model,
                    Minecraft.getMinecraft().getTextureMapBlocks()
                            .getAtlasSprite(materials[0].getTexture(EnumFrameTexture.BORDER).toString())).makeBakedModel()
                                    .getGeneralQuads());
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {

        return model.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {

        return model.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {

        return model.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {

        return model.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {

        return model.getItemCameraTransforms();
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return this;
        IFrameMaterial[] materials = new IFrameMaterial[3];
        if (tag.hasKey("border")) materials[0] = FrameRegistry.INSTANCE.materials.get(tag.getString("border"));
        if (tag.hasKey("cross")) materials[1] = FrameRegistry.INSTANCE.materials.get(tag.getString("cross"));
        if (tag.hasKey("binding")) materials[2] = FrameRegistry.INSTANCE.materials.get(tag.getString("binding"));
        return new ModelFramePanel(model, materials);
    }

}
