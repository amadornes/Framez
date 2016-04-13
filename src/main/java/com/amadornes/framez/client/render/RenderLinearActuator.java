package com.amadornes.framez.client.render;

import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.ModelTransformer;
import com.amadornes.framez.client.ModelTransformer.IVertexTransformer;
import com.amadornes.framez.motor.MotorLogicLinearActuator;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class RenderLinearActuator extends RenderMotor<MotorLogicLinearActuator> {

    @Override
    public void renderMotor(MotorLogicLinearActuator logic, double x, double y, double z, float partialTicks, int destroyStage,
            WorldRenderer wr) {

        final double extension = (Math.sin(System.currentTimeMillis() / 400D) + 1) / 2D;
        TileMotor motor = logic.getMotor();
        BlockPos pos = motor.getMotorPos();
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState state;
        wr.setTranslation(x - pos.getX(), y - pos.getY() - extension, z - pos.getZ());
        state = motor.getMotorWorld().getBlockState(pos);
        state = state.getBlock().getActualState(state, motor.getMotorWorld(), pos).withProperty(BlockMotor.PROPERTY_PART_TYPE, 1);
        IBakedModel model = brd.getBlockModelShapes().getModelForState(state);
        // TODO: if (model instanceof ISmartBlockModel)
        // model = ((ISmartBlockModel) model).handleBlockState(state.getBlock().getExtendedState(state, motor.getMotorWorld(), pos));
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(), model, state, pos, wr);
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(),
                brd.getBlockModelShapes().getModelForState(Blocks.sandstone.getDefaultState()), Blocks.sandstone.getDefaultState(),
                pos.down(), wr);
        wr.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        state = state.withProperty(BlockMotor.PROPERTY_PART_TYPE, 2);
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(),
                ModelTransformer.transform(brd.getBlockModelShapes().getModelForState(state), new IVertexTransformer() {

                    @Override
                    public float[] transform(EnumType type, EnumUsage usage, float... data) {

                        if (usage == EnumUsage.POSITION) {
                            if (data[1] <= 0.0) {
                                data[1] = 0.25F;
                            } else if (data[1] >= 1.0) {
                                data[1] = (float) (0.25F - extension);
                            }
                        }

                        return data;
                    }
                }, wr.getVertexFormat()), state, pos, wr);

        wr.setTranslation(0, 0, 0);
    }

}
