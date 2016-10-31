package com.amadornes.framez.client.model;

import java.util.Arrays;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class AdvancedModelRextexturer {

    public static IBakedModel retexture(IBlockState state, long rand, IBakedModel model, TextureAtlasSprite texture) {
        return retexture(state, rand, model, new TextureAtlasSprite[] { texture, texture, texture, texture, texture, texture, texture });
    }

    public static IBakedModel retexture(IBlockState state, long rand, IBakedModel model, TextureAtlasSprite[] textures) {
        IBakedModel result = new IPerspectiveAwareModel() {

            @Override
            public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
                return (List) Arrays.asList(model.getQuads(state, side, rand).stream()
                        .map(q -> new AdvancedBreakingFour(q, textures[q.getFace() == null ? 6 : q.getFace().ordinal()])).toArray());
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
            public boolean isAmbientOcclusion() {

                return model.isAmbientOcclusion();
            }

            @Override
            public TextureAtlasSprite getParticleTexture() {

                return textures[0];
            }

            @Override
            public ItemCameraTransforms getItemCameraTransforms() {

                return model.getItemCameraTransforms();
            }

            @Override
            public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

                return model instanceof IPerspectiveAwareModel ? ((IPerspectiveAwareModel) model).handlePerspective(cameraTransformType)
                        : Pair.of(this, null);
            }

            @Override
            public ItemOverrideList getOverrides() {

                return model.getOverrides();
            }
        };
        return result;
    }

    private static class AdvancedBreakingFour extends BakedQuad {

        private final TextureAtlasSprite texture;

        public AdvancedBreakingFour(BakedQuad quad, TextureAtlasSprite textureIn) {

            super(Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length), quad.getTintIndex(),
                    FaceBakery.getFacingFromVertexData(quad.getVertexData()), textureIn, quad.shouldApplyDiffuseLighting(),
                    quad.getFormat());
            this.texture = textureIn;
            this.remapQuad(quad);
        }

        private void remapQuad(BakedQuad quad) {

            for (int i = 0; i < 4; ++i) {
                this.remapVert(quad, i);
            }
        }

        private void remapVert(BakedQuad quad, int vertex) {

            TextureAtlasSprite texture = quad.getSprite();
            if (texture == null) {
                return;
            }

            int i = 7 * vertex;
            float u = (Float.intBitsToFloat(this.vertexData[i + 4]) - texture.getInterpolatedU(0)) * 16F
                    / (texture.getInterpolatedU(16) - texture.getInterpolatedU(0));
            float v = (Float.intBitsToFloat(this.vertexData[i + 5]) - texture.getInterpolatedV(0)) * 16F
                    / (texture.getInterpolatedV(16) - texture.getInterpolatedV(0));
            this.vertexData[i + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(u));
            this.vertexData[i + 5] = Float.floatToRawIntBits(this.texture.getInterpolatedV(v));
        }
    }

}
