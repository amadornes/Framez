package com.amadornes.framez.item;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.amadornes.framez.api.movement.IFrameMaterial;
import com.amadornes.framez.client.GeneratedTexture;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrafting extends ItemFramez {

    private IFrameMaterial material;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite cross, panel, blocked;

    public ItemCrafting(IFrameMaterial material) {

        this.material = material;

        setCreativeTab(FramezCreativeTab.tab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "Crafting Item";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        return StatCollector
                .translateToLocal("item." + ModInfo.MODID + ":crafting.name")
                .replace(
                        "%mat.adj%",
                        FramezConfig.simple_mode ? StatCollector.translateToLocalFormatted("tile." + ModInfo.MODID + ":frame.name",
                                StatCollector.translateToLocal("misc." + ModInfo.MODID + ":support")) : StatCollector
                                .translateToLocal("material." + ModInfo.MODID + ":" + material.getType() + ".adj"))
                .replace(
                        "%type%",
                        StatCollector.translateToLocal("item."
                                + ModInfo.MODID
                                + ":crafting."
                                + (stack.getItemDamage() == 0 ? "cross" : (stack.getItemDamage() == 1 ? "panel"
                                        : (stack.getItemDamage() == 2 ? "blocked" : "error"))) + ".name"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        try {
            tryRegisterIcons(reg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public void tryRegisterIcons(IIconRegister reg) throws Exception {

        if (reg instanceof TextureMap) {
            String idCross = ModInfo.MODID + ":frame/" + material.getType() + "/cross";
            String idPanel = ModInfo.MODID + ":frame/" + material.getType() + "/panel";
            String idBlocked = ModInfo.MODID + ":frame/" + material.getType() + "/blocked";

            BufferedImage original = ImageIO.read(getClass().getResourceAsStream(
                    "/assets/" + ModInfo.MODID + "/textures/blocks/frame/" + material.getType() + ".png"));
            BufferedImage originalCross = original.getSubimage(0, original.getHeight() / 2, original.getWidth() / 2,
                    original.getHeight() / 2);

            BufferedImage imgCross = new BufferedImage(original.getWidth() / 2, original.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = imgCross.createGraphics();
            g.drawImage(originalCross, 0, 0, null);
            g.dispose();
            cross = new GeneratedTexture(idCross, imgCross, null);

            BufferedImage imgPanel = new BufferedImage(original.getWidth() / 2, original.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
            g = imgPanel.createGraphics();
            g.drawImage(originalCross, 0, 0, null);
            g.drawImage(original.getSubimage(0, 0, original.getWidth() / 2, original.getHeight() / 2), 0, 0, null);
            g.dispose();
            panel = new GeneratedTexture(idPanel, imgPanel, null);

            BufferedImage imgBlocked = new BufferedImage(original.getWidth() / 2, original.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
            g = imgBlocked.createGraphics();
            g.drawImage(original.getSubimage(original.getWidth() / 2, original.getHeight() / 2, original.getWidth() / 2,
                    original.getHeight() / 2), 0, 0, null);
            g.drawImage(original.getSubimage(0, 0, original.getWidth() / 2, original.getHeight() / 2), 0, 0, null);
            g.dispose();
            blocked = new GeneratedTexture(idBlocked, imgBlocked, null);

            TextureMap map = (TextureMap) reg;
            map.setTextureEntry(idCross, cross);
            map.setTextureEntry(idPanel, panel);
            map.setTextureEntry(idBlocked, blocked);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {

        return meta == 0 ? cross : (meta == 1 ? panel : (meta == 2 ? blocked : null));
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item i, CreativeTabs t, List l) {

        l.add(new ItemStack(i, 1, 0));
        l.add(new ItemStack(i, 1, 1));
        // l.add(new ItemStack(i, 1, 2));
    }

}
