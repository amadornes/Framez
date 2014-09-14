package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.amadornes.framez.init.CreativeTabFramez;
import com.amadornes.framez.ref.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFramePart extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon cross;
    @SideOnly(Side.CLIENT)
    private IIcon frame;
    @SideOnly(Side.CLIENT)
    private IIcon crossIron;
    @SideOnly(Side.CLIENT)
    private IIcon frameIron;

    public ItemFramePart() {

        setCreativeTab(CreativeTabFramez.inst);
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        cross = reg.registerIcon(References.Textures.FRAME_PART_CROSS);
        frame = reg.registerIcon(References.Textures.FRAME_PART_FRAME);
        crossIron = reg.registerIcon(References.Textures.FRAME_PART_CROSS_IRON);
        frameIron = reg.registerIcon(References.Textures.FRAME_PART_FRAME_IRON);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

        return getIconIndex(stack);
    }

    @Override
    public IIcon getIconIndex(ItemStack is) {

        switch (is.getItemDamage()) {
        case 0:
            return cross;
        case 1:
            return frame;
        case 2:
            return crossIron;
        case 3:
            return frameIron;
        }

        return null;
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {

        switch (is.getItemDamage()) {
        case 0:
            return "item." + References.Names.Unlocalized.FRAME_PART_CROSS;
        case 1:
            return "item." + References.Names.Unlocalized.FRAME_PART_FRAME;
        case 2:
            return "item." + References.Names.Unlocalized.FRAME_PART_CROSS_IRON;
        case 3:
            return "item." + References.Names.Unlocalized.FRAME_PART_FRAME_IRON;
        }

        return "<ERROR>";
    }

}
