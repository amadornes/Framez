package com.amadornes.framez.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings({ "deprecation", "unchecked" })
public class ModelTransformer {

    public static IBakedModel transform(IBakedModel model, IVertexTransformer transformer, IBlockState state, long rand) {

        List<BakedQuad>[] quads = new List[7];
        for (int i = 0; i < quads.length; i++) {
            quads[i] = new ArrayList<BakedQuad>();
            for (BakedQuad quad : model.getQuads(state, (i == 6 ? null : EnumFacing.getFront(i)), rand))
                quads[i].add(transform(quad, transformer));
        }
        return new TransformedModel(model, quads);
    }

    private static BakedQuad transform(BakedQuad quad, IVertexTransformer transformer) {

        // TODO: Optimize
        VertexFormat format = quad.getFormat();
        Field f = ReflectionHelper.findField(UnpackedBakedQuad.class, "unpackedData");
        f.setAccessible(true);
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        if (quad.hasTintIndex()) builder.setQuadTint(quad.getTintIndex());
        builder.setQuadOrientation(quad.getFace());
        LightUtil.putBakedQuad(builder, quad);
        UnpackedBakedQuad unpackedQuad = builder.build();
        try {
            float[][][] unpackedData = (float[][][]) f.get(unpackedQuad);
            int count = format.getElementCount();
            for (int v = 0; v < 4; v++) {
                for (int e = 0; e < count; e++) {
                    VertexFormatElement element = format.getElement(e);
                    unpackedData[v][e] = transformer.transform(quad, element.getType(), element.getUsage(), unpackedData[v][e]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unpackedQuad;
    }

    private static final class TransformedModel implements IBakedModel {

        private final IBakedModel parent;
        private final List<BakedQuad>[] quads;

        public TransformedModel(IBakedModel parent, List<BakedQuad>[] quads) {

            this.parent = parent;
            this.quads = quads;
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

            return quads[side == null ? 6 : side.ordinal()];
        }

        @Override
        public boolean isAmbientOcclusion() {

            return parent.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {

            return parent.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {

            return parent.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {

            return parent.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {

            return parent.getItemCameraTransforms();
        }

        @Override
        public ItemOverrideList getOverrides() {

            return parent.getOverrides();
        }

    }

    public static interface IVertexTransformer {

        public float[] transform(BakedQuad quad, EnumType type, EnumUsage usage, float... data);

    }

}