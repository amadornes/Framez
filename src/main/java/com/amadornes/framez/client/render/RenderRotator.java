package com.amadornes.framez.client.render;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.EnumMotorStatus;
import com.amadornes.framez.block.BlockMotor;
import com.amadornes.framez.client.ModelTransformer;
import com.amadornes.framez.client.ModelTransformer.IVertexTransformer;
import com.amadornes.framez.motor.logic.MotorLogicRotator;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.math.BlockPos;

public class RenderRotator extends RenderMotor<MotorLogicRotator> {

    @Override
    public void renderMotor(MotorLogicRotator logic, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer wr) {

        int ticks = logic.getMotor().getCurrentMovementTicks();
        int totalTicks = logic.getMotor().getVariable(TileMotor.MOVEMENT_TIME);
        double progress = (ticks + partialTicks) / (double) totalTicks;
        final double angle;
        if (logic.getMotor().checkStatus(EnumMotorStatus.MOVING) && logic.getAction() != null) {
            if (ticks == -1) {
                progress = 1;
            }
            angle = (logic.getAction() == EnumMotorAction.ROTATE_CCLOCKWISE ? progress : -progress) * 90;
        } else {
            angle = 0;
        }
        TileMotor motor = logic.getMotor();
        BlockPos pos = motor.getMotorPos();
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState state;
        wr.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        state = motor.getMotorWorld().getBlockState(pos);
        state = state.getActualState(motor.getMotorWorld(), pos).withProperty(BlockMotor.PROPERTY_PART_TYPE, 1);
        IVertexTransformer transformer = (quad, type, usage, data) -> {

            if (usage == EnumUsage.POSITION) {
                Vector3f pos1 = new Vector3f(data);
                pos1.sub(new Vector3f(0.5F, 0.5F, 0.5F));
                Matrix4f mat = new Matrix4f();
                mat.rotY((float) Math.toRadians(angle));
                mat.transform(pos1);
                pos1.add(new Vector3f(0.5F, 0.5F, 0.5F));
                pos1.get(data);
            }

            return data;
        };
        brd.getBlockModelRenderer().renderModel(motor.getMotorWorld(),
                ModelTransformer.transform(brd.getBlockModelShapes().getModelForState(state), transformer, state, 0L), state, pos, wr,
                false);
        wr.setTranslation(0, 0, 0);
    }

}
