package com.amadornes.framez.client.model;

import java.util.Arrays;
import java.util.List;

import com.amadornes.framez.client.ClientProxy;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class AdvancedModelRextexturer {

    public static IBakedModel retexture(IBakedModel model, TextureAtlasSprite texture) {

        List<BakedQuad> generalQuads = (List) Arrays
                .asList(model.getGeneralQuads().stream().map(q -> new AdvancedBreakingFour(q, texture)).toArray());
        List<BakedQuad>[] faceQuads = new List[6];
        for (int i = 0; i < 6; i++)
            faceQuads[i] = (List) Arrays
                    .asList(model.getFaceQuads(EnumFacing.getFront(i)).stream().map(q -> new AdvancedBreakingFour(q, texture)).toArray());
        return new IBakedModel() {

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

                return texture;
            }

            @Override
            public ItemCameraTransforms getItemCameraTransforms() {

                return model.getItemCameraTransforms();
            }

            @Override
            public List<BakedQuad> getGeneralQuads() {

                return generalQuads;
            }

            @Override
            public List<BakedQuad> getFaceQuads(EnumFacing face) {

                return faceQuads[face.ordinal()];
            }
        };
    }

    private static class AdvancedBreakingFour extends BakedQuad {

        private final TextureAtlasSprite texture;

        public AdvancedBreakingFour(BakedQuad quad, TextureAtlasSprite textureIn) {

            super(Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length), quad.getTintIndex(),
                    FaceBakery.getFacingFromVertexData(quad.getVertexData()));
            this.texture = textureIn;
            this.remapQuad();
        }

        private void remapQuad() {

            for (int i = 0; i < 4; ++i)
                this.remapVert(i);
        }

        private void remapVert(int vertex) {

            int i = 7 * vertex;
            float u = (Float.intBitsToFloat(this.vertexData[i + 4]) * ClientProxy.BLOCK_TEXTURE_WIDTH) % 16F;
            float v = (Float.intBitsToFloat(this.vertexData[i + 5]) * ClientProxy.BLOCK_TEXTURE_HEIGHT) % 16F;
            this.vertexData[i + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(u));
            this.vertexData[i + 5] = Float.floatToRawIntBits(this.texture.getInterpolatedV(v));
        }
    }

}
