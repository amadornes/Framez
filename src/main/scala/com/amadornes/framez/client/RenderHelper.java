package com.amadornes.framez.client;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.BlockRenderer.BlockFace;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderPipeline.PipelineBuilder;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.CCRenderState.IVertexSource;
import codechicken.lib.render.uv.MultiIconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.TransformationList;
import codechicken.lib.vec.Translation;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.api.movement.IFrameRenderData;
import com.amadornes.framez.api.movement.IFrameRenderData.IFrameTexture;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.ref.References;
import com.amadornes.framez.util.layout.Layout;
import com.amadornes.trajectory.api.vec.BlockPos;

public class RenderHelper {

    private static final RenderHelper instance = new RenderHelper();

    public static RenderHelper instance() {

        return instance;
    }

    private static final BlockFace face = new BlockRenderer.BlockFace();

    public PipelineBuilder builder;
    private IVertexSource model;
    private IIcon override = null;
    public TransformationList list = new TransformationList();
    private IBlockAccess w;
    private int x, y, z;

    public RenderHelper start(IBlockAccess w, int x, int y, int z, int pass) {

        start();

        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;

        if (pass != -1)
            builder.add(CCRenderState.lightMatrix);
        CCRenderState.setBrightness(w, x, y, z);
        CCRenderState.lightMatrix.locate(w, x, y, z);

        builder.add(list = new TransformationList(new Translation(x, y, z)));

        return this;
    }

    public RenderHelper start() {

        builder = CCRenderState.pipeline.builder();

        this.w = null;
        this.x = this.y = this.z = 0;

        return this;
    }

    public RenderHelper render() {

        CCRenderState.setModel(model);

        builder.render();

        return this;
    }

    public RenderHelper setColor(int argb) {

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 0) & 0xFF;

        return setColor(r, g, b, a);
    }

    public RenderHelper setColor(int rgb, int alpha) {

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;

        return setColor(r, g, b, alpha);
    }

    public RenderHelper setColor(int rgb, double alpha) {

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;

        return setColor(r / 255D, g / 255D, b / 255D, alpha);
    }

    public RenderHelper setColor(int red, int green, int blue, int alpha) {

        return setColor(red / 255D, green / 255D, blue / 255D, alpha / 255D);
    }

    public RenderHelper setColor(double red, double green, double blue, double alpha) {

        builder.add(new RenderColor(red, green, blue, alpha));

        return this;
    }

    public RenderHelper renderFaceX(double y1, double z1, double y2, double z2, double x, IIcon texture, boolean positive) {

        if (override != null)
            texture = override;

        model = face.computeLightCoords();
        face.loadCuboidFace(new Cuboid6(x, y1, z1, x, y2, z2), positive ? 5 : 4);
        builder.add(new MultiIconTransformation(texture));
        render();

        return this;
    }

    public RenderHelper renderFaceY(double x1, double z1, double x2, double z2, double y, IIcon texture, boolean positive) {

        if (override != null)
            texture = override;

        model = face.computeLightCoords();
        face.loadCuboidFace(new Cuboid6(x1, y, z1, x2, y, z2), positive ? 1 : 0);
        builder.add(new MultiIconTransformation(texture));
        render();

        return this;
    }

    public RenderHelper renderFaceZ(double x1, double y1, double x2, double y2, double z, IIcon texture, boolean positive) {

        if (override != null)
            texture = override;

        model = face.computeLightCoords();
        face.loadCuboidFace(new Cuboid6(x1, y1, z, x2, y2, z), positive ? 3 : 2);
        builder.add(new MultiIconTransformation(texture));
        render();

        return this;
    }

    public RenderHelper renderBox(AxisAlignedBB box, IIcon down, IIcon up, IIcon north, IIcon south, IIcon west, IIcon east) {

        if (override != null)
            down = up = north = south = west = east = override;

        return render(CCModel.quadModel(24).generateBlock(0, new Cuboid6(box)).computeNormals().computeLightCoords(), down, up, north,
                south, west, east);
    }

    public RenderHelper renderBox(AxisAlignedBB box, IIcon icon) {

        return renderBox(box, icon, icon, icon, icon, icon, icon);
    }

    public RenderHelper render(CCModel model, IIcon down, IIcon up, IIcon north, IIcon south, IIcon west, IIcon east) {

        if (override != null) {
            if (down != IconSupplier.empty)
                down = override;
            if (up != IconSupplier.empty)
                up = override;
            if (north != IconSupplier.empty)
                north = override;
            if (south != IconSupplier.empty)
                south = override;
            if (west != IconSupplier.empty)
                west = override;
            if (east != IconSupplier.empty)
                east = override;
        }
        if (down == null)
            down = IconSupplier.empty;
        if (up == null)
            up = IconSupplier.empty;
        if (north == null)
            north = IconSupplier.empty;
        if (south == null)
            south = IconSupplier.empty;
        if (west == null)
            west = IconSupplier.empty;
        if (east == null)
            east = IconSupplier.empty;

        if (model == null)
            return this;

        this.model = model;
        builder.add(new MultiIconTransformation(down, up, north, south, west, east));
        render();
        return this;
    }

    public static CCModel frame3DBorderIn = null;
    public static CCModel frame3DBorderOut = null;
    public static CCModel frame3DInsideOut = null;
    public static CCModel frame3DInsideIn = null;

    public static CCModel[] latching = new CCModel[2];
    public static CCModel[] sticky = new CCModel[2];
    public static CCModel redface = null;

    public static CCModel[][] motorArrowSlider = new CCModel[6][4];
    public static CCModel[][][] motorArrowSliderBouncy = new CCModel[3][6][4];

    public static CCModel[][] motorArrowRotation = new CCModel[6][2];

    public static CCModel[][] motorArrowLinearActuator = new CCModel[6][4];

    public static CCModel[][] motorArrowBlinkDrive = new CCModel[6][4];

    public static CCModel[] motorLineFace = new CCModel[6];

    private static final Transformation[] transformations = new Transformation[2 * 2 * 2 * 2 * 2 * 2];

    static {
        frame3DBorderIn = CCModel
                .combine(
                        Arrays.asList(
                                CCModel.quadModel(4 * 4)
                                        .generateBlock(0, new Cuboid6(2 / 16D, 0, 2 / 16D, 14 / 16D, 1, 14 / 16D), (1 << 0) | (1 << 1))
                                        .backfacedCopy(),
                                CCModel.quadModel(4 * 4)
                                        .generateBlock(0, new Cuboid6(0, 2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D), (1 << 4) | (1 << 5))
                                        .backfacedCopy(),
                                CCModel.quadModel(4 * 4)
                                        .generateBlock(0, new Cuboid6(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1), (1 << 2) | (1 << 3))
                                        .backfacedCopy())).shrinkUVs(-0.0005).computeNormals().computeLightCoords();
        frame3DBorderOut = CCModel.quadModel(6 * 4).generateBlock(0, new Cuboid6(0, 0, 0, 1, 1, 1)).shrinkUVs(-0.0005).computeNormals()
                .computeLightCoords();
        frame3DInsideOut = CCModel.quadModel(6 * 4).generateBlock(0, new Cuboid6(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D))
                .computeNormals().computeLightCoords();
        frame3DInsideIn = frame3DInsideOut.backfacedCopy().computeNormals().computeLightCoords();

        double tr = 0.005;
        for (int bottom = 0; bottom <= 1; bottom++) {
            for (int top = 0; top <= 1; top++) {
                for (int north = 0; north <= 1; north++) {
                    for (int south = 0; south <= 1; south++) {
                        for (int west = 0; west <= 1; west++) {
                            for (int east = 0; east <= 1; east++) {
                                int h = bottom + top * 2 + north * 4 + south * 8 + west * 16 + east * 32;

                                Scale s = new Scale(1 + (1 - west * 2) * tr + (1 - east * 2 - 1) * tr, 1 + (1 - bottom * 2) * tr
                                        + (1 - top * 2) * tr, 1 + (1 - north * 2) * tr + (1 - south * 2) * tr);
                                Translation t2 = new Translation(0.5 + (west - east) * tr, 0.5 + (bottom - top) * tr, 0.5 + (north - south)
                                        * tr);

                                transformations[h] = new TransformationList(new Translation(-0.5, -0.5, -0.5), s, t2);
                            }
                        }
                    }
                }
            }
        }

        Transformation t = new Translation(0, 0, 0);
        Transformation t_ = new Translation(-0.5, -0.5, -0.5).with(new Rotation(90 * MathHelper.torad, 0, 1, 0)).with(
                new Translation(0.5, 0.5, 0.5));

        List<CCModel> latchingModels1 = new ArrayList<CCModel>();
        List<CCModel> latchingModels2 = new ArrayList<CCModel>();

        Cuboid6 latchingCubeA = new Cuboid6(3 / 32D, 1 - 1 / 64D, 3 / 32D, 2 / 16D + 1 / 64D, 1, 29 / 32D);// Visible
        Cuboid6 latchingCubeB = new Cuboid6(3 / 32D, 0, 3 / 32D, 2 / 16D + 1 / 64D, 1 / 32D, 29 / 32D);// Visible
        Cuboid6 latchingCube1 = new Cuboid6(3 / 32D, 0.002, 3 / 32D, 2 / 16D + 1 / 64D, 1 / 32D, 29 / 32D);// Hidden
        Cuboid6 latchingCube2A = new Cuboid6(0, 3 / 256D, 0, 1 / 32D, 13 / 256D, 1);
        Cuboid6 latchingCube2B = new Cuboid6(1 - 1 / 256D, 3 / 256D, 0, 1, 13 / 256D, 1);

        for (int i = 0; i < 4; i++) {
            t = t.with(t_);

            latchingModels1.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCubeA).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(new Translation(0, -1, 0)).apply(t));
            latchingModels1.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCubeB).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(t));
            latchingModels1.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCube2A).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(t));
            latchingModels1.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCube2B).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(new Translation(-1, 0, 0)).apply(t));

            latchingModels2.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCube1).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(t));
            latchingModels2.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCube2A).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(t));
            latchingModels2.add(CCModel.quadModel(6 * 4).generateBlock(0, latchingCube2B).shrinkUVs(0.0005).computeNormals()
                    .computeLightCoords().apply(new Translation(-1, 0, 0)).apply(t));
        }

        latching[0] = CCModel.combine(latchingModels1);
        latching[1] = CCModel.combine(latchingModels2);

        Cuboid6 stickyCubeA = new Cuboid6(2 / 32D, 1 - 1 / 32D, 2 / 32D, 30 / 32D, 1, 30 / 32D);// Visible
        Cuboid6 stickyCubeB = new Cuboid6(2 / 32D, 0.002, 2 / 32D, 30 / 32D, 1 / 32D, 30 / 32D);

        sticky[0] = CCModel.quadModel(6 * 4).generateBlock(0, stickyCubeA).shrinkUVs(0.0005).computeNormals().computeLightCoords();
        sticky[1] = CCModel.quadModel(6 * 4).generateBlock(0, stickyCubeB).shrinkUVs(0.0005).computeNormals().computeLightCoords();

        redface = CCModel.quadModel(6 * 4)
                .generateBlock(0, new Cuboid6(2 / 16D, 1 / 16D - 0.01, 2 / 16D, 14 / 16D, 1 / 16D + 0.01, 14 / 16D)).shrinkUVs(0.0005)
                .computeNormals().computeLightCoords();

    }

    public void reloadModels() {

        String baseSlider = "/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_ARROW_SLIDER;
        String baseRotator = "/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_ARROW_ROTATOR;
        String baseLinearActivator = "/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_ARROW_LINEAR_ACTUATOR;
        String baseBlinkDrive = "/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_ARROW_BLINK_DRIVE;

        computeVariants(motorArrowSlider, loadModel(baseSlider, 0xFF0000));

        computeVariants(motorArrowSliderBouncy[0], loadModel(baseSlider + "_double", 0xFF0000));
        computeVariants(motorArrowSliderBouncy[1], loadModel(baseSlider + "_double", 0x00FF00));
        computeVariants(motorArrowSliderBouncy[2], loadModel(baseSlider + "_double", 0x0000FF));

        motorArrowRotation[0][0] = loadModel(baseRotator, 0xFF0000, true, 60 / 64D, 63 / 64D);
        motorArrowRotation[0][1] = motorArrowRotation[0][0].backfacedCopy().apply(new Translation(-0.5, -0.5, -0.5))
                .apply(new Scale(-1, 1, 1)).apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords();

        for (int i = 1; i < 6; i++) {
            motorArrowRotation[i][0] = motorArrowRotation[0][0].copy().apply(new Translation(-0.5, -0.5, -0.5))
                    .apply(Rotation.sideRotations[i]).apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords();
            motorArrowRotation[i][1] = motorArrowRotation[0][1].copy().apply(new Translation(-0.5, -0.5, -0.5))
                    .apply(Rotation.sideRotations[i]).apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords();
        }

        computeVariants(motorArrowLinearActuator, loadModel(baseLinearActivator, 0xFF0000));

        computeVariants(motorArrowBlinkDrive, loadModel(baseBlinkDrive, 0xFF0000));

        CCModel m = loadModel("/assets/" + ModInfo.MODID + "/textures/blocks/" + References.Texture.MOTOR_LINE_FACE, 0xFF0000);
        motorLineFace[0] = CCModel.combine(Arrays.asList(
                m,
                m.copy().apply(new Translation(-0.5, -0.5, -0.5)).apply(new Rotation(MathHelper.torad * 90, 0, 1, 0))
                        .apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords()));

        for (int i = 1; i < 6; i++)
            motorLineFace[i] = motorLineFace[0].copy().apply(new Translation(-0.5, -0.5, -0.5)).apply(Rotation.sideRotations[i])
                    .apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords();
    }

    private CCModel loadModel(String path, int color) {

        return loadModel(path, color, false, 1 / 64D, 63 / 64D);
    }

    private CCModel loadModel(String path, int color, boolean top, double distMin, double distMax) {

        List<CCModel> models1 = new ArrayList<CCModel>();

        Layout layout = new Layout(path);
        double pixel = 1 / (double) layout.getLayout(color).getWidth();
        for (Rectangle r : layout.getSimplifiedLayout(color).getRectangles())
            models1.add(CCModel.quadModel(6 * 4).generateBlock(
                    0,
                    new Cuboid6(r.getMinX() * pixel, top ? distMin : 1 - r.getMaxY() * pixel, !top ? distMin : 1 - r.getMaxY() * pixel, r
                            .getMaxX() * pixel, top ? distMax : 1 - r.getMinY() * pixel, !top ? distMax : 1 - r.getMinY() * pixel)));

        CCModel model = CCModel.combine(models1).computeNormals().computeLightCoords();
        models1.clear();

        return model;
    }

    private void computeVariants(CCModel[][] models, CCModel base) {

        for (int f = 0; f < 6; f++)
            for (int i = 0; i < 4; i++)
                models[f][i] = base.copy().apply(new Translation(-0.5, -0.5, -0.5)).apply(new Rotation(MathHelper.torad * 90 * i, 0, 1, 0))
                        .apply(Rotation.sideRotations[f]).apply(new Translation(0.5, 0.5, 0.5)).computeNormals().computeLightCoords();
    }

    // public boolean renderFrame(IFrameRenderData renderData) {
    //
    // if (renderData == null)
    // return false;
    //
    // boolean result = renderFrame_do(renderData);
    //
    // if (renderData.getModifiers() != null && renderData.getFrame() != null)
    // for (IFrameModifier m : renderData.getModifiers())
    // if (m instanceof IFrameModifierOverlay)
    // result |= renderFrame_do(new ModifierRenderData(renderData.getFrame(), (IFrameModifierOverlay) m));
    //
    // return result;
    // }

    public boolean renderFrame(IBlockAccess world, BlockPos position, IFrameMaterial material, IFrameRenderData renderData, int pass) {

        if (material == null)
            return false;

        IFrameTexture[] texturesCrossIn = new IFrameTexture[] {//
        material.getTexture(renderData, 0, 0),//
                material.getTexture(renderData, 1, 0),//
                material.getTexture(renderData, 2, 0),//
                material.getTexture(renderData, 3, 0),//
                material.getTexture(renderData, 4, 0),//
                material.getTexture(renderData, 5, 0) //
        };
        IFrameTexture[] texturesBorderIn = new IFrameTexture[] {//
        material.getTexture(renderData, 0, 1),//
                material.getTexture(renderData, 1, 1),//
                material.getTexture(renderData, 2, 1),//
                material.getTexture(renderData, 3, 1),//
                material.getTexture(renderData, 4, 1),//
                material.getTexture(renderData, 5, 1) //
        };
        IFrameTexture[] texturesCrossOut = new IFrameTexture[] {//
        material.getTexture(renderData, 0, 2),//
                material.getTexture(renderData, 1, 2),//
                material.getTexture(renderData, 2, 2),//
                material.getTexture(renderData, 3, 2),//
                material.getTexture(renderData, 4, 2),//
                material.getTexture(renderData, 5, 2) //
        };
        IFrameTexture[] texturesBorderOut = new IFrameTexture[] {//
        material.getTexture(renderData, 0, 3),//
                material.getTexture(renderData, 1, 3),//
                material.getTexture(renderData, 2, 3),//
                material.getTexture(renderData, 3, 3),//
                material.getTexture(renderData, 4, 3),//
                material.getTexture(renderData, 5, 3) //
        };

        return renderFrame(world, position, texturesCrossIn, texturesBorderIn, texturesCrossOut, texturesBorderOut, renderData, pass);
    }

    public boolean renderFrame(IBlockAccess world, BlockPos position, IFrameTexture[] texturesCrossIn, IFrameTexture[] texturesBorderIn,
            IFrameTexture[] texturesCrossOut, IFrameTexture[] texturesBorderOut, IFrameRenderData renderData, int pass) {

        boolean clickThrough = FramezConfig.click_through_frames && world != null;

        if (pass == 1 && !clickThrough && override == null)
            return false;

        int h = (renderData.isSideHidden(0) ? 1 : 0) + (renderData.isSideHidden(1) ? 2 : 0) + (renderData.isSideHidden(2) ? 4 : 0)
                + (renderData.isSideHidden(3) ? 8 : 0) + (renderData.isSideHidden(4) ? 16 : 0) + (renderData.isSideHidden(5) ? 32 : 0);

        boolean covers = renderData.canHaveCovers();
        CCModel frame3DBorderOut = covers ? RenderHelper.frame3DBorderOut.copy().apply(transformations[h]) : RenderHelper.frame3DBorderOut;
        CCModel frame3DBorderIn = covers ? RenderHelper.frame3DBorderIn.copy().apply(transformations[h]) : RenderHelper.frame3DBorderIn;
        CCModel frame3DInsideOut = covers ? RenderHelper.frame3DInsideOut.copy().apply(transformations[h]) : RenderHelper.frame3DInsideOut;
        CCModel frame3DInsideIn = covers ? RenderHelper.frame3DInsideIn.copy().apply(transformations[h]) : RenderHelper.frame3DInsideIn;

        if (!clickThrough || pass == 1) {
            setColor(clickThrough ? 0x7FFFFFFF : 0xFFFFFFFF);
            render(frame3DInsideOut, !renderData.shouldRenderCross(0) ? IconSupplier.empty
                    : (texturesCrossOut[0] == null ? IconSupplier.empty : texturesCrossOut[0].cross(renderData.isSideBlocked(0))),//
                    !renderData.shouldRenderCross(1) ? IconSupplier.empty : (texturesCrossOut[1] == null ? IconSupplier.empty
                            : texturesCrossOut[1].cross(renderData.isSideBlocked(1))),//
                    !renderData.shouldRenderCross(2) ? IconSupplier.empty : (texturesCrossOut[2] == null ? IconSupplier.empty
                            : texturesCrossOut[2].cross(renderData.isSideBlocked(2))),//
                    !renderData.shouldRenderCross(3) ? IconSupplier.empty : (texturesCrossOut[3] == null ? IconSupplier.empty
                            : texturesCrossOut[3].cross(renderData.isSideBlocked(3))),//
                    !renderData.shouldRenderCross(4) ? IconSupplier.empty : (texturesCrossOut[4] == null ? IconSupplier.empty
                            : texturesCrossOut[4].cross(renderData.isSideBlocked(4))), //
                    !renderData.shouldRenderCross(5) ? IconSupplier.empty : (texturesCrossOut[5] == null ? IconSupplier.empty
                            : texturesCrossOut[5].cross(renderData.isSideBlocked(5))));
            if (override == null)
                render(frame3DInsideIn, !renderData.shouldRenderCross(0) ? IconSupplier.empty
                        : (texturesCrossIn[0] == null ? IconSupplier.empty : texturesCrossIn[0].cross(renderData.isSideBlocked(0))),//
                        !renderData.shouldRenderCross(1) ? IconSupplier.empty : (texturesCrossIn[1] == null ? IconSupplier.empty
                                : texturesCrossIn[1].cross(renderData.isSideBlocked(1))),//
                        !renderData.shouldRenderCross(2) ? IconSupplier.empty : (texturesCrossIn[2] == null ? IconSupplier.empty
                                : texturesCrossIn[2].cross(renderData.isSideBlocked(2))),//
                        !renderData.shouldRenderCross(3) ? IconSupplier.empty : (texturesCrossIn[3] == null ? IconSupplier.empty
                                : texturesCrossIn[3].cross(renderData.isSideBlocked(3))),//
                        !renderData.shouldRenderCross(4) ? IconSupplier.empty : (texturesCrossIn[4] == null ? IconSupplier.empty
                                : texturesCrossIn[4].cross(renderData.isSideBlocked(4))), //
                        !renderData.shouldRenderCross(5) ? IconSupplier.empty : (texturesCrossIn[5] == null ? IconSupplier.empty
                                : texturesCrossIn[5].cross(renderData.isSideBlocked(5))));
            setColor(0xFFFFFFFF);
        }

        if (override == null)
            render(frame3DBorderOut, //
                    texturesBorderOut[0] == null ? IconSupplier.empty : texturesBorderOut[0].border(renderData.hasPanel(0)), //
                    texturesBorderOut[1] == null ? IconSupplier.empty : texturesBorderOut[1].border(renderData.hasPanel(1)), //
                    texturesBorderOut[2] == null ? IconSupplier.empty : texturesBorderOut[2].border(renderData.hasPanel(2)), //
                    texturesBorderOut[3] == null ? IconSupplier.empty : texturesBorderOut[3].border(renderData.hasPanel(3)), //
                    texturesBorderOut[4] == null ? IconSupplier.empty : texturesBorderOut[4].border(renderData.hasPanel(4)), //
                    texturesBorderOut[5] == null ? IconSupplier.empty : texturesBorderOut[5].border(renderData.hasPanel(5)));
        if (override == null)
            render(frame3DBorderIn, //
                    texturesBorderIn[0] == null ? IconSupplier.empty : texturesBorderIn[0].border(false), //
                    texturesBorderIn[1] == null ? IconSupplier.empty : texturesBorderIn[1].border(false), //
                    texturesBorderIn[2] == null ? IconSupplier.empty : texturesBorderIn[2].border(false),//
                    texturesBorderIn[3] == null ? IconSupplier.empty : texturesBorderIn[3].border(false), //
                    texturesBorderIn[4] == null ? IconSupplier.empty : texturesBorderIn[4].border(false), //
                    texturesBorderIn[5] == null ? IconSupplier.empty : texturesBorderIn[5].border(false));

        return true;
    }

    public void setOverrideTexture(IIcon texture) {

        override = texture;
    }

    public void reset() {

        CCRenderState.reset();
        model = null;
    }

    public RenderHelper addTranslation(double x, double y, double z) {

        list.with(new Translation(x, y, z));
        return this;
    }

    public RenderHelper addRotation(double x, double y, double z) {

        list.with(new Translation(-this.x, -this.y, -this.z));

        if ((x % 360) != 0) {
            x *= MathHelper.torad;
            list.with(new Rotation(x, 1, 0, 0));
        }
        if ((y % 360) != 0) {
            y *= MathHelper.torad;
            list.with(new Rotation(y, 0, 1, 0));
        }
        if ((z % 360) != 0) {
            z *= MathHelper.torad;
            list.with(new Rotation(z, 0, 0, 1));
        }

        list.with(new Translation(this.x, this.y, this.z));
        return this;
    }

    public RenderHelper addRotation(int face) {

        list.with(new Translation(-this.x, -this.y, -this.z));

        if (face == 4 || face == 5)
            list.with(new Rotation(90 * MathHelper.torad, 0, 1, 0));
        list.with(Rotation.sideRotations[face]);

        list.with(new Translation(this.x, this.y, this.z));

        return this;
    }

    public RenderHelper remRotation(int face) {

        list.with(Rotation.sideRotations[face].inverse());
        if (face == 4 || face == 5)
            list.with(new Rotation(-90 * MathHelper.torad, 0, 1, 0));
        return this;
    }

    public RenderHelper addScale(double x, double y, double z) {

        list.with(new Scale(x, y, z));
        return this;
    }

    public IBlockAccess getCurrentBlockAccess() {

        return w;
    }

    public int getCurrentX() {

        return x;
    }

    public int getCurrentY() {

        return y;
    }

    public int getCurrentZ() {

        return z;
    }

    public void tmpOverrideLighting(IBlockAccess w, int x, int y, int z) {

        CCRenderState.lightMatrix.locate(w, x, y, z);
        CCRenderState.setBrightness(w, x, y, z);
    }

    public void resetLighting() {

        CCRenderState.lightMatrix.locate(w, x, y, z);
        CCRenderState.setBrightness(w, x, y, z);
    }
}
