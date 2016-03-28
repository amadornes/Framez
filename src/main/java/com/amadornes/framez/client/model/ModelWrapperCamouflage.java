package com.amadornes.framez.client.model;

import java.util.List;

import com.amadornes.framez.block.BlockMotor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class ModelWrapperCamouflage implements ISmartBlockModel {

    private IBakedModel model;
    private IBlockState[] faces;

    public ModelWrapperCamouflage(IBakedModel model) {

        this.model = model;
        this.faces = new IBlockState[6];
    }

    private ModelWrapperCamouflage(IBakedModel model, IBlockState[] faces) {

        this.model = model;
        this.faces = faces;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing face) {

        if (faces[face.ordinal()] != null) {
            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                    .getModelForState(faces[face.ordinal()]);
            if (model != null) return model.getFaceQuads(face);
        }
        return model.getFaceQuads(face);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {

        return model.getGeneralQuads();
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
    public IBakedModel handleBlockState(IBlockState state) {

        if (!(state instanceof IExtendedBlockState)
                || !((IExtendedBlockState) state).getUnlistedNames().contains(BlockMotor.PROPERTY_CAMO_DOWN))
            return this;
        IExtendedBlockState s = (IExtendedBlockState) state;
        return new ModelWrapperCamouflage(model,
                new IBlockState[] { s.getValue(BlockMotor.PROPERTY_CAMO_DOWN), s.getValue(BlockMotor.PROPERTY_CAMO_UP),
                        s.getValue(BlockMotor.PROPERTY_CAMO_NORTH), s.getValue(BlockMotor.PROPERTY_CAMO_SOUTH),
                        s.getValue(BlockMotor.PROPERTY_CAMO_WEST), s.getValue(BlockMotor.PROPERTY_CAMO_EAST) });
    }

}
