package com.amadornes.framez.client.model;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

@SuppressWarnings("deprecation")
public class ModelMotorRotationHandler implements IBakedModel {

    private IBakedModel model;

    public ModelMotorRotationHandler(IBakedModel model) {

        this.model = model;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        // TRSRTransformation.getMatrix(facing);

        return model.getQuads(state, side, rand);
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
    public ItemOverrideList getOverrides() {

        return model.getOverrides();
    }

}
