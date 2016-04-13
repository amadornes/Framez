package com.amadornes.framez.client;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

@SuppressWarnings({ "deprecation", "unchecked" })
public class ModelTransformer {

    public static IBakedModel transform(IBakedModel model, IVertexTransformer transformer, VertexFormat format) {

        return transform(model, transformer, IVertexFormatTransformer.NONE, format);
    }

    public static IBakedModel transform(IBakedModel model, IVertexTransformer transformer, IVertexFormatTransformer vfTransformer,
            VertexFormat original) {

        VertexFormat format = vfTransformer.getNewFormat(original);
        int[] map = LightUtil.mapFormats(original, format);
        List<BakedQuad>[] quads = new List[7];
        for (int i = 0; i < quads.length; i++) {
            quads[i] = new ArrayList<BakedQuad>();
            for (BakedQuad quad : (i == 6 ? model.getGeneralQuads() : model.getFaceQuads(EnumFacing.getFront(i))))
                quads[i].add(transform(quad, transformer, vfTransformer, original, format, map));
        }
        return new TransformedModel(model, quads, format);
    }

    private static BakedQuad transform(BakedQuad quad, IVertexTransformer transformer, IVertexFormatTransformer vfTransformer,
            final VertexFormat original, final VertexFormat format, int[] map) {

        final float[][][] unpackedData = new float[4][original.getElementCount()][4];
        quad.pipe(new IVertexConsumer() {

            int vertices = 0;
            int elements = 0;

            @Override
            public void setQuadTint(int tint) {

            }

            @Override
            public void setQuadOrientation(EnumFacing orientation) {

            }

            @Override
            public void setQuadColored() {

            }

            @Override
            public void put(int element, float... data) {

                for (int i = 0; i < 4; i++) {
                    if (i < data.length) unpackedData[vertices][element][i] = data[i];
                    else unpackedData[vertices][element][i] = 0;
                }
                elements++;
                if (elements == original.getElementCount()) {
                    vertices++;
                    elements = 0;
                }
            }

            @Override
            public VertexFormat getVertexFormat() {

                return original;
            }
        });

        float[][][] newData = new float[4][format.getElementCount()][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < original.getElementCount(); j++) {
                VertexFormatElement element = original.getElement(j);
                newData[i][map[j]] = transformer.transform(element.getType(), element.getUsage(), unpackedData[i][j]);
            }
        }

        return new UnpackedBakedQuad(vfTransformer.remap(original, newData), quad.getTintIndex(), quad.getFace(), format);
    }

    private static final class TransformedModel implements IBakedModel, IFlexibleBakedModel {

        private final IBakedModel parent;
        private final List<BakedQuad>[] quads;
        private final VertexFormat format;

        public TransformedModel(IBakedModel parent, List<BakedQuad>[] quads, VertexFormat format) {

            this.parent = parent;
            this.quads = quads;
            this.format = format;
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing facing) {

            return quads[facing.ordinal()];
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {

            return quads[6];
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
        public VertexFormat getFormat() {

            return format;
        }

    }

    public static interface IVertexTransformer {

        public float[] transform(EnumType type, EnumUsage usage, float... data);

    }

    public static interface IVertexFormatTransformer {

        public static final IVertexFormatTransformer NONE = new IVertexFormatTransformer() {

            @Override
            public VertexFormat getNewFormat(VertexFormat original) {

                return original;
            }

            @Override
            public float[][][] remap(VertexFormat original, float[][][] data) {

                return data;
            }

        };

        public static final IVertexFormatTransformer COMPUTE_NORMALS = new IVertexFormatTransformer() {

            private final WeakHashMap<VertexFormat, VertexFormat> formats = new WeakHashMap<VertexFormat, VertexFormat>();

            @Override
            public VertexFormat getNewFormat(VertexFormat original) {

                if (original.hasNormal()) return original;
                VertexFormat format = formats.get(original);
                if (format != null) return format;
                formats.put(original, format = new VertexFormat(original));
                format.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.NORMAL, 3));
                return format;
            }

            @Override
            public float[][][] remap(VertexFormat original, float[][][] data) {

                VertexFormat format = getNewFormat(original);
                int posIndex = 0, normalIndex = 0;
                for (int i = 0; i < format.getElementCount(); i++) {
                    if (format.getElement(i).isPositionElement()) posIndex = i;
                    if (format.getElement(i).getUsage() == EnumUsage.NORMAL) normalIndex = i;
                }
                Vector3f a = new Vector3f(data[0][posIndex][0], data[0][posIndex][1], data[0][posIndex][2]);
                Vector3f b = new Vector3f(data[1][posIndex][0], data[1][posIndex][1], data[1][posIndex][2]);
                Vector3f c = new Vector3f(data[2][posIndex][0], data[2][posIndex][1], data[2][posIndex][2]);
                a.sub(b);
                c.sub(b);
                b.cross(a, c);
                for (int i = 0; i < 4; i++) {
                    data[i][normalIndex][0] = b.x;
                    data[i][normalIndex][1] = b.y;
                    data[i][normalIndex][2] = b.z;
                }

                return data;
            }

        };

        public VertexFormat getNewFormat(VertexFormat original);

        public float[][][] remap(VertexFormat original, float[][][] data);

    }

}