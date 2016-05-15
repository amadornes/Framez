package com.amadornes.framez.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import com.amadornes.framez.api.movement.IFrameRenderData.IFrameTexture;

public class FrameTexture implements IFrameTexture {

    private String resourceLocation;
    private IIcon base;
    private IIcon border, border_panel, cross, blocked, full;

    public FrameTexture(String resourceLocation) {

        this.resourceLocation = resourceLocation;
    }

    @Override
    public IIcon border(boolean panel) {

        return panel ? this.border_panel : this.border;
    }

    @Override
    public IIcon cross(boolean blocked) {

        return blocked ? this.blocked : this.cross;
    }

    @Override
    public IIcon full() {

        return full;
    }

    public String getResourceLocation() {

        return resourceLocation;
    }

    public void registerIcons(IIconRegister register) {

        base = register.registerIcon(resourceLocation);
        border = new FrameIcon(0, 0);
        border_panel = new FrameIcon(1, 0);
        cross = new FrameIcon(0, 1);
        blocked = new FrameIcon(1, 1);

        // Generate a texture for the frame cross + border
        try {
            BufferedImage textureMap = ImageIO.read(getClass().getResourceAsStream(
                    "/assets/" + resourceLocation.replace(":", "/textures/blocks/") + ".png"));
            BufferedImage imgFull = new BufferedImage(textureMap.getWidth() / 2, textureMap.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = imgFull.createGraphics();
            g.drawImage(textureMap.getSubimage(0, textureMap.getHeight() / 2, textureMap.getWidth() / 2, textureMap.getHeight() / 2), 0, 0,
                    null);
            g.drawImage(textureMap.getSubimage(0, 0, textureMap.getWidth() / 2, textureMap.getHeight() / 2), 0, 0, null);
            g.dispose();
            full = new GeneratedTexture(base.getIconName() + "_full", imgFull, null);

            ((TextureMap) register).setTextureEntry(base.getIconName() + "_full", (GeneratedTexture) full);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private class FrameIcon implements IIcon {

        private final int x, y;

        public FrameIcon(int x, int y) {

            this.x = x;
            this.y = y;
        }

        @Override
        public int getIconWidth() {

            return base.getIconWidth() / 2;
        }

        @Override
        public int getIconHeight() {

            return base.getIconHeight() / 2;
        }

        @Override
        public float getMinU() {

            return getInterpolatedU(0);
        }

        @Override
        public float getMinV() {

            return getInterpolatedV(0);
        }

        @Override
        public float getMaxU() {

            return getInterpolatedV(16);
        }

        @Override
        public float getMaxV() {

            return getInterpolatedV(16);
        }

        @Override
        public float getInterpolatedU(double d) {

            return base.getInterpolatedU((x == 1 ? 8 : 0) + (d / 2D));
        }

        @Override
        public float getInterpolatedV(double d) {

            return base.getInterpolatedV((y == 1 ? 8 : 0) + (d / 2D));
        }

        @Override
        public String getIconName() {

            return base.getIconName() + "_" + x + "_" + y;
        }

        @Override
        public String toString() {

            return "FrameIcon{name=\'" + this.getIconName() + '\'' + ", height=" + this.getIconHeight() + ", width=" + this.getIconWidth()
                    + ", u0=" + this.getMinU() + ", u1=" + this.getMaxU() + ", v0=" + this.getMinV() + ", v1=" + this.getMaxV() + '}';
        }

    }

}
