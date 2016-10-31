package com.amadornes.framez.client.render;

import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.ModelTransformer;
import com.amadornes.framez.motor.logic.MotorLogicLinearActuator;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.math.BlockPos;

public class RenderLinearActuator extends RenderMotor<MotorLogicLinearActuator> {

    @Override
    public void renderMotor(MotorLogicLinearActuator logic, double x, double y, double z, float partialTicks, int destroyStage,
            VertexBuffer wr) {

        int ticks = logic.getMotor().getCurrentMovementTicks();
        int totalTicks = logic.getMotor().getVariable(TileMotor.MOVEMENT_TIME);
        double progress = (ticks + partialTicks) / (double) totalTicks;
        final double extension;
        if (logic.getAction() != null) {
            if (ticks == -1) {
                progress = 1;
            }
            extension = (logic.getAction() == EnumMotorAction.MOVE_BACKWARD ? 1 - progress : progress);
        } else {
            extension = 0;
        }
        TileMotor motor = logic.getMotor();
        BlockPos pos = motor.getMotorPos();
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState state;
        wr.setTranslation(x - pos.getX(), y - pos.getY() - extension, z - pos.getZ());
        state = motor.getMotorWorld().getBlockState(pos);
        state = state.getActualState(motor.getMotorWorld(), pos).withProperty(BlockMotor.PROPERTY_PART_TYPE, 1);
        IBakedModel model = brd.getBlockModelShapes().getModelForState(state);
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(), model, state, pos, wr, false);
        wr.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        state = state.withProperty(BlockMotor.PROPERTY_PART_TYPE, 2);
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(),
                ModelTransformer.transform(brd.getBlockModelShapes().getModelForState(state), (quad, type, usage, data) -> {

                    if (usage == EnumUsage.POSITION) {
                        if (data[1] <= 0.0) {
                            data[1] = 0.25F;
                        } else if (data[1] >= 1.0) {
                            data[1] = (float) (0.25F - extension);
                        }
                    }

                    return data;
                }, state, 0L), state, pos, wr, false);

        wr.setTranslation(0, 0, 0);
    }

}
