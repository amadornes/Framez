package com.amadornes.framez.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IColoredBakedQuad;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings({ "deprecation", "unchecked" })
public class ModelTransformer {

    public static IBakedModel transform(IBakedModel model, IVertexTransformer transformer, VertexFormat format) {

        return transform(model, transformer, IVertexFormatTransformer.NONE, format);
    }

    public static IBakedModel transform(IBakedModel model, IVertexTransformer transformer, IVertexFormatTransformer vfTransformer,
            VertexFormat original) {

        VertexFormat newFormat = vfTransformer.getNewFormat(original);
        List<BakedQuad>[] quads = new List[7];
        for (int i = 0; i < quads.length; i++) {
            quads[i] = new ArrayList<BakedQuad>();
            for (BakedQuad quad : (i == 6 ? model.getGeneralQuads() : model.getFaceQuads(EnumFacing.getFront(i))))
                quads[i].add(transform(quad, transformer, vfTransformer, original, newFormat));
        }
        return new TransformedModel(model, quads, newFormat);
    }

    private static BakedQuad transform(BakedQuad quad, IVertexTransformer transformer, IVertexFormatTransformer vfTransformer,
            final VertexFormat original, final VertexFormat format) {

        // TODO: Optimize
        Field f = ReflectionHelper.findField(UnpackedBakedQuad.class, "unpackedData");
        f.setAccessible(true);
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        if (quad.hasTintIndex()) builder.setQuadTint(quad.getTintIndex());
        if (quad instanceof IColoredBakedQuad) builder.setQuadColored();
        builder.setQuadOrientation(quad.getFace());
        LightUtil.putBakedQuad(builder, quad);
        UnpackedBakedQuad unpackedQuad = builder.build();
        try {
            float[][][] unpackedData = (float[][][]) f.get(unpackedQuad);
            int count = format.getElementCount();
            for (int v = 0; v < 4; v++) {
                for (int e = 0; e < count; e++) {
                    VertexFormatElement element = format.getElement(e);
                    unpackedData[v][e] = transformer.transform(element.getType(), element.getUsage(), unpackedData[v][e]);
                }
            }
            vfTransformer.remap(original, unpackedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unpackedQuad;
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

                if (original.hasColor()) return original;
                VertexFormat format = formats.get(original);
                if (format != null) return format;
                formats.put(original, format = new VertexFormat(original));
                format.addElement(DefaultVertexFormats.COLOR_4UB);
                return format;
            }

            @Override
            public float[][][] remap(VertexFormat original, float[][][] data) {

                VertexFormat format = getNewFormat(original);
                int posIndex = 0, colorIndex = 0;
                for (int i = 0; i < format.getElementCount(); i++) {
                    if (format.getElement(i).isPositionElement()) posIndex = i;
                    if (format.getElement(i).getUsage() == EnumUsage.COLOR) colorIndex = i;
                }
                Vector3f a = new Vector3f(data[0][posIndex][0], data[0][posIndex][1], data[0][posIndex][2]);
                Vector3f b = new Vector3f(data[1][posIndex][0], data[1][posIndex][1], data[1][posIndex][2]);
                Vector3f c = new Vector3f(data[2][posIndex][0], data[2][posIndex][1], data[2][posIndex][2]);
                a.sub(b);
                c.sub(b);
                b.cross(c, a);
                b.normalize();
                float brightness = getBrightness(b);
                for (int i = 0; i < 4; i++) {
                    data[i][colorIndex][0] = brightness;
                    data[i][colorIndex][1] = brightness;
                    data[i][colorIndex][2] = brightness;
                }

                return data;
            }

            private float getBrightness(Vector3f normal) {

                float x = getFaceBrightness(EnumFacing.getFacingFromVector(normal.x, 0, 0));
                float y = getFaceBrightness(EnumFacing.getFacingFromVector(0, normal.y, 0));
                float z = getFaceBrightness(EnumFacing.getFacingFromVector(0, 0, normal.z));
                return x * normal.x * normal.x + y * normal.y * normal.y + z * normal.z * normal.z;
            }

            private float getFaceBrightness(EnumFacing facing) {

                switch (facing) {
                case DOWN:
                    return 0.5F;
                case UP:
                    return 1.0F;
                case NORTH:
                case SOUTH:
                    return 0.8F;
                case WEST:
                case EAST:
                    return 0.6F;
                default:
                    return 1.0F;
                }
            }

        };

        public VertexFormat getNewFormat(VertexFormat original);

        public float[][][] remap(VertexFormat original, float[][][] data);

    }

}