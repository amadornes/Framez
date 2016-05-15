package com.amadornes.framez.client.render;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCRenderState;

import com.amadornes.framez.client.IconSupplier;
import com.amadornes.framez.client.LightingNB;
import com.amadornes.framez.client.LightingNBNormals;
import com.amadornes.framez.client.RenderHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMotorCore implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        CCRenderState.reset();
        CCRenderState.useNormals = true;
        CCRenderState.pullLightmap();
        GL11.glPushMatrix();
        {
            CCRenderState.changeTexture(TextureMap.locationBlocksTexture);
            if (type == ItemRenderType.INVENTORY)
                GL11.glTranslated(0, -0.09375, 0);
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(-0.5, -0.5, -0.5);
            CCRenderState.startDrawing();

            RenderHelper renderer = RenderHelper.instance();
            renderer.start();
            renderCore(item.getItemDamage(), type);
            renderer.reset();

            CCRenderState.draw();
        }
        GL11.glPopMatrix();
        CCRenderState.reset();

        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderCore(int meta, ItemRenderType type) {

        RenderHelper renderer = RenderHelper.instance();

        IIcon border = IconSupplier.motor_border;
        IIcon center = IconSupplier.motor_center;
        IIcon arrow_base = IconSupplier.arrow_base;
        IIcon[] border_outer = new IIcon[] { border, border, border, border, border, border };

        renderer.start();

        renderer.render(RenderHelper.frame3DBorderIn, border, border, border, border, border, border);
        renderer.render(RenderHelper.frame3DBorderOut, border_outer[0], border_outer[1], border_outer[2], border_outer[3], border_outer[4],
                border_outer[5]);
        renderer.render(RenderHelper.frame3DInsideOut, center, center, center, center, center, center);

        if (type == null) {
            renderer.reset();
            renderer.start();
        }

        if (type == null)
            renderer.builder.add(new LightingNBNormals());
        if (type != ItemRenderType.INVENTORY)
            renderer.builder.add(new LightingNB());

        // Render arrows
        {
            if (meta >= 1 && meta <= 4) {
                renderer.render(RenderHelper.motorLineFace[0], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base, arrow_base);

                if (meta == 1) {
                    renderer.render(RenderHelper.motorArrowSlider[0][1], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                } else if (meta == 2) {
                    renderer.render(RenderHelper.motorArrowRotation[0][1], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                } else if (meta == 3) {
                    renderer.render(RenderHelper.motorArrowLinearActuator[0][0], arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base, arrow_base);
                    renderer.render(RenderHelper.motorArrowLinearActuator[0][1], arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base, arrow_base);
                } else if (meta == 4) {
                    renderer.render(RenderHelper.motorArrowBlinkDrive[0][0], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                    renderer.render(RenderHelper.motorArrowBlinkDrive[0][1], arrow_base, arrow_base, arrow_base, arrow_base, arrow_base,
                            arrow_base);
                }
            }
        }

        renderer.reset();
    }
}
