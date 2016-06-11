package com.amadornes.framez.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.amadornes.framez.client.ModelTransformer;
import com.amadornes.framez.util.EnumIconTypes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

@SuppressWarnings("deprecation")
public class ModelMotorDecorationHandler implements IBakedModel, IPerspectiveAwareModel {

    private IBakedModel model;
    private int id;
    private IBakedModel[] decorations = null;

    public ModelMotorDecorationHandler(IBakedModel model, int id) {

        this.model = model;
        this.id = id;
    }

    private void genDecorations(int id, boolean isInWorld) {

        float dist = (0.6875F / 2F + 7) / 16F;

        // Linear actuator
        if (id == 0) {
            IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(EnumIconTypes.DOWN_NB.getIconStack(),
                    null, null);
            final Matrix4f mat = new Matrix4f();
            mat.setIdentity();
            mat.setScale(0.6875F);

            mat.setTranslation(new Vector3f(0, 2 / 16F, dist));
            decorations[EnumFacing.SOUTH.ordinal()] = transform(model, mat, isInWorld);

            mat.setTranslation(new Vector3f(0, 2 / 16F, -dist));
            decorations[EnumFacing.NORTH.ordinal()] = transform(model, mat, isInWorld);

            mat.rotY((float) Math.toRadians(90));
            mat.setScale(0.6875F);

            mat.setTranslation(new Vector3f(dist, 2 / 16F, 0));
            decorations[EnumFacing.EAST.ordinal()] = transform(model, mat, isInWorld);

            mat.setTranslation(new Vector3f(-dist, 2 / 16F, 0));
            decorations[EnumFacing.WEST.ordinal()] = transform(model, mat, isInWorld);
        } else
        // Rotator
        if (id == 1) {
            IBakedModel model = Minecraft.getMinecraft().getRenderItem()
                    .getItemModelWithOverrides(EnumIconTypes.CLOCKWISE_NB.getIconStack(), null, null);
            final Matrix4f mat = new Matrix4f();
            mat.setIdentity();
            mat.rotX((float) Math.toRadians(-90));
            mat.setTranslation(new Vector3f(0, dist, 0));
            mat.setScale(0.6875F);
            decorations[EnumFacing.UP.ordinal()] = transform(model, mat, isInWorld);
        } else
        // Slider
        if (id == 2) {
            // Top
            {
                IBakedModel model = Minecraft.getMinecraft().getRenderItem()
                        .getItemModelWithOverrides(EnumIconTypes.FORWARD_NB.getIconStack(), null, null);
                final Matrix4f mat = new Matrix4f();
                mat.setIdentity();
                mat.rotY((float) Math.toRadians(90));
                Matrix4f mat2 = new Matrix4f();
                mat2.setIdentity();
                mat2.rotX((float) Math.toRadians(-90));
                mat.mul(mat2);
                mat.setTranslation(new Vector3f(0, dist, 0));
                mat.setScale(0.6875F);
                decorations[EnumFacing.UP.ordinal()] = transform(model, mat, isInWorld);
            }
            // Sides
            {
                IBakedModel model = Minecraft.getMinecraft().getRenderItem()
                        .getItemModelWithOverrides(EnumIconTypes.FORWARD_NB.getIconStack(), null, null);
                final Matrix4f mat = new Matrix4f();
                mat.setIdentity();
                mat.rotY((float) Math.toRadians(90));
                mat.setScale(0.6875F);

                mat.setTranslation(new Vector3f(-dist, 2 / 16F, 0));
                decorations[EnumFacing.WEST.ordinal()] = transform(model, mat, isInWorld);

                mat.setTranslation(new Vector3f(dist, 2 / 16F, 0));
                decorations[EnumFacing.EAST.ordinal()] = transform(model, mat, isInWorld);
            }
        }
    }

    private IBakedModel transform(IBakedModel model, Matrix4f mat, boolean isInWorld) {

        return ModelTransformer.transform(model, (quad, type, usage, data) -> {
            if (usage == EnumUsage.POSITION) {
                Point3f point = new Point3f(data[0] - 0.5F, data[1] - 0.5F, data[2] - 0.5F);
                mat.transform(point);
                data[0] = point.x + 0.5F;
                data[1] = point.y + 0.5F;
                data[2] = point.z + 0.5F;
            } else if (usage == EnumUsage.NORMAL) {
                Vector3f vec = new Vector3f(data);
                vec.normalize();
                mat.transform(vec);
                vec.normalize();
                data[0] = vec.x;
                data[1] = vec.y;
                data[2] = vec.z;
            } else if (usage == EnumUsage.UV && type == EnumType.SHORT) {
                data[0] = 15 * 32.0f / 0xffff;
                data[1] = 15 * 32.0f / 0xffff;
            }
            return data;
        }, null, 0L, f -> isInWorld ? new VertexFormat(f).addElement(DefaultVertexFormats.TEX_2S) : f);

    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

        if (decorations == null) {
            decorations = new IBakedModel[6];
            genDecorations(id, state != null);
        }

        IBakedModel decor = side == null ? null : decorations[side.ordinal()];
        if (decor == null) return model.getQuads(state, side, rand);

        List<BakedQuad> list = new ArrayList<BakedQuad>();
        list.addAll(model.getQuads(state, side, rand));
        list.addAll(decor.getQuads(state, null, rand));
        return list;
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

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

        if (model instanceof IPerspectiveAwareModel) {
            Pair<? extends IBakedModel, Matrix4f> p = ((IPerspectiveAwareModel) model).handlePerspective(cameraTransformType);
            if (p != null) return Pair.of(this, p.getRight());
        }
        return Pair.of(this, null);
    }

}
