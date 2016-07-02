package com.amadornes.framez.motor.placement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;

public class MotorPlacementFace implements IMotorPlacement<EnumFacing> {

    @Override
    public EnumFacing getPlacementData(EntityPlayer player, RayTraceResult hit) {

        return hit.sideHit.getOpposite();
    }

    @Override
    public void renderPlacementArrow(EnumFacing data) {

        // EnumFacing off = data.getOpposite();
        // GL11.glTranslated(off.getFrontOffsetX() * 0.01, off.getFrontOffsetY() * 0.01, off.getFrontOffsetZ() * 0.01);
        // float u1 = 0, u2 = 0, v1 = 256, v2 = 256;
        //
        // Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        //
        // GL11.glEnable(GL11.GL_TEXTURE_2D);
        //
        // VertexBuffer vb = Tessellator.getInstance().getBuffer();
        // vb.reset();
        // vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        // vb.pos(0, 0, 0).tex(u1, v1).endVertex();
        // vb.pos(0, 0, 1).tex(u1, v2).endVertex();
        // vb.pos(1, 0, 1).tex(u2, v2).endVertex();
        // vb.pos(1, 0, 0).tex(u2, v1).endVertex();
        // Tessellator.getInstance().draw();

    }

}
