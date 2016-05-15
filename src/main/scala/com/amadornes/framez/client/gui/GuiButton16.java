package com.amadornes.framez.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.amadornes.framez.ref.ModInfo;

public class GuiButton16 extends GuiButton {

    public static final ResourceLocation buttonTextures = new ResourceLocation(ModInfo.MODID, "textures/gui/buttons.png");

    public GuiButton16(int id, int x, int y) {

        this(id, x, y, 16);
    }

    public GuiButton16(int id, int x, int y, int w) {

        this(id, x, y, w, null);
    }

    public GuiButton16(int id, int x, int y, String text) {

        this(id, x, y, 16, text);
    }

    public GuiButton16(int id, int x, int y, int w, String text) {

        super(id, x, y, w, 16, text);
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {

        if (this.visible) {
            p_146112_1_.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition
                    && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2,
                    this.height);
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);

            if (displayString != null) {
                FontRenderer fontrenderer = p_146112_1_.fontRenderer;
                int l = 14737632;
                if (packedFGColour != 0)
                    l = packedFGColour;
                else if (!this.enabled)
                    l = 10526880;
                else if (this.field_146123_n)
                    l = 16777120;
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition
                        + (this.height - 8) / 2, l);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {

        return this.enabled && p_146116_2_ >= this.xPosition && p_146116_3_ >= this.yPosition && p_146116_2_ < this.xPosition + this.width
                && p_146116_3_ < this.yPosition + this.height;
    }

}
