package com.amadornes.framez.client.render;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.ModelTransformer;
import com.amadornes.framez.client.ModelTransformer.IVertexTransformer;
import com.amadornes.framez.motor.logic.MotorLogicRotator;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class RenderRotator extends RenderMotor<MotorLogicRotator> {

    @Override
    public void renderMotor(MotorLogicRotator logic, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer wr) {

        double t = 10;
        final double angle = (System.currentTimeMillis() % (t * 360)) / t;
        TileMotor motor = logic.getMotor();
        BlockPos pos = motor.getMotorPos();
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState state;
        wr.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        state = motor.getMotorWorld().getBlockState(pos);
        state = state.getBlock().getActualState(state, motor.getMotorWorld(), pos).withProperty(BlockMotor.PROPERTY_PART_TYPE, 1);
        IVertexTransformer transformer = new IVertexTransformer() {

            @Override
            public float[] transform(BakedQuad quad, EnumType type, EnumUsage usage, float... data) {

                if (usage == EnumUsage.POSITION) {
                    Vector3f pos = new Vector3f(data);
                    pos.sub(new Vector3f(0.5F, 0.5F, 0.5F));
                    Matrix4f mat = new Matrix4f();
                    mat.rotY((float) Math.toRadians(angle));
                    mat.transform(pos);
                    pos.add(new Vector3f(0.5F, 0.5F, 0.5F));
                    pos.get(data);
                }

                return data;
            }
        };
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(),
                ModelTransformer.transform(brd.getBlockModelShapes().getModelForState(state), transformer, state, 0L), state, pos, wr,
                false);
        brd.getBlockModelRenderer()
                .renderModel(motor.getMotorWorld(), ModelTransformer
                        .transform(brd.getBlockModelShapes().getModelForState(Blocks.SANDSTONE.getDefaultState()), transformer, null, 0),
                        Blocks.SANDSTONE.getDefaultState(), pos.down(), wr, false);

        wr.setTranslation(0, 0, 0);
    }

}