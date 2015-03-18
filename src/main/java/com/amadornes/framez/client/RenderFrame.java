package com.amadornes.framez.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.config.Config;
import com.amadornes.framez.item.ItemPartFrame;
import com.amadornes.framez.part.PartFrame;

public class RenderFrame implements IItemRenderer {

    public static boolean renderFrame3D(RenderHelper renderer, IIcon[] borders_, IIcon border, IIcon[] cross_, IIcon[] simple_,
            boolean[] hidden, int pass) {

        if (Config.simple_frames) { // Only 2D render config option
            if (pass == 0) {
                return renderFrame2D(renderer, Config.click_through_frames ? borders_ : simple_, hidden);
            } else if (pass == 1 && Config.click_through_frames) {
                renderer.setOpacity(0.5);
                boolean result = renderFrame2D(renderer, simple_, hidden);
                renderer.setOpacity(1);
                return result;
            }
            return false;
        }

        IIcon[] borders = new IIcon[6];
        IIcon[] cross = new IIcon[6];
        IIcon[] simple = new IIcon[6];
        for (int i = 0; i < 6; i++) {
            int pos = i;
            if (pos > 1)
                pos = 2 + (pos % 4);
            borders[pos] = borders_[i];
            cross[pos] = cross_[i];
            simple[pos] = simple_[i];
        }

        double d = 0.0025D / 2D;

        Translation tr = new Translation((hidden[4] ? d : 0) - (hidden[5] ? d : 0), (hidden[0] ? d : 0) - (hidden[1] ? d : 0),
                (hidden[2] ? d : 0) - (hidden[3] ? d : 0));
        Scale sc = new Scale((1 - d * 2) + (!hidden[4] ? d * 2 : 0) + (!hidden[5] ? d * 2 : 0), (1 - d * 2) + (!hidden[0] ? d * 2 : 0)
                + (!hidden[1] ? d * 2 : 0), (1 - d * 2) + (!hidden[2] ? d * 2 : 0) + (!hidden[3] ? d * 2 : 0));

        if (pass == 0) {
            renderer.addTransformation(tr);
            renderer.addTransformation(sc);

            renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), borders);

            renderer.setRenderFromInside(true);
            {
                renderer.renderBox(new Vec3dCube(0, 2 / 16D, 2 / 16D, 1, 14 / 16D, 14 / 16D), border);
                renderer.renderBox(new Vec3dCube(2 / 16D, 0, 2 / 16D, 14 / 16D, 1, 14 / 16D), border);
                renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 1), border);
            }
            renderer.setRenderFromInside(false);
            if (!Config.click_through_frames) {
                renderer.setRenderFromInside(true);
                renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), cross);
                renderer.setRenderFromInside(false);
                renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), cross);
            }

            renderer.removeTransformations(2);

            return true;
        } else if (pass == 1 && Config.click_through_frames) {
            renderer.addTransformation(tr);
            renderer.addTransformation(sc);

            renderer.setOpacity(0.5);
            renderer.setRenderFromInside(true);
            renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), cross);
            renderer.setRenderFromInside(false);
            renderer.renderBox(new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 15 / 16D, 15 / 16D), cross);
            renderer.setOpacity(1);

            renderer.removeTransformations(2);
            return true;
        }
        return false;
    }

    public static boolean renderFrame2D(RenderHelper renderer, IIcon[] simple_, boolean[] hidden) {

        IIcon[] simple = new IIcon[6];
        for (int i = 0; i < 6; i++) {
            int pos = i;
            if (pos > 1)
                pos = 2 + (pos % 4);
            simple[pos] = simple_[i];
        }

        double d = 0.0025D / 2D;

        Translation tr = new Translation((hidden[4] ? d : 0) - (hidden[5] ? d : 0), (hidden[0] ? d : 0) - (hidden[1] ? d : 0),
                (hidden[2] ? d : 0) - (hidden[3] ? d : 0));
        Scale sc = new Scale((1 - d * 2) + (!hidden[4] ? d * 2 : 0) + (!hidden[5] ? d * 2 : 0), (1 - d * 2) + (!hidden[0] ? d * 2 : 0)
                + (!hidden[1] ? d * 2 : 0), (1 - d * 2) + (!hidden[2] ? d * 2 : 0) + (!hidden[3] ? d * 2 : 0));

        renderer.addTransformation(tr);
        renderer.addTransformation(sc);

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), simple);
        renderer.setRenderFromInside(true);
        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), simple);
        renderer.setRenderFromInside(false);

        renderer.removeTransformations(2);

        return true;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    public static final PartFrame renderer = new PartFrame();

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.INVENTORY)
                GL11.glTranslated(0, -0.0625, 0);
            if (type == ItemRenderType.ENTITY) {
                GL11.glScaled(0.5, 0.5, 0.5);
                GL11.glTranslated(-0.5, -0.45, -0.5);
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            IFrame f = ((IFrame) ((ItemPartFrame) item.getItem()).newPart(item, Framez.proxy.getPlayer(), Framez.proxy.getWorld(), null, 0,
                    null));
            if (f != null)
                f.renderItem(item, type);
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);

    }

}
