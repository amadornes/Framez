package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.amadornes.framez.Framez;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.util.FramezUtils;

public class ItemBlockFramez extends ItemBlock {

    private String name = "error";

    public ItemBlockFramez(Block block) {

        super(block);
    }

    public ItemBlockFramez(Block block, String name) {

        super(block);

        this.name = name;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "tile." + ModInfo.MODID + ":" + name;
    }

    protected String getUnlocalizedTip(ItemStack stack, EntityPlayer player) {

        return getUnlocalizedName(stack).replace("tile.", "tooltip.");
    }

    @SideOnly(Side.CLIENT)
    protected void addTipInformation(ItemStack stack, EntityPlayer player, List<String> l, String tip) {

        l.addAll(FramezUtils.split(tip, 35));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List l, boolean unused) {

        String unlocalized = getUnlocalizedTip(stack, player);
        String localized = I18n.format(unlocalized);
        if (!hasTooltip(stack, player, unlocalized, localized)) return;

        if (!Framez.proxy.isShiftDown()) {
            l.add(I18n.format("tooltip." + ModInfo.MODID + ":shift", EnumChatFormatting.GRAY, EnumChatFormatting.YELLOW + ""
                    + EnumChatFormatting.ITALIC, EnumChatFormatting.RESET + "" + EnumChatFormatting.GRAY));
        } else {
            addTipInformation(stack, player, l, localized);
        }
    }

    protected boolean hasTooltip(ItemStack stack, EntityPlayer player, String unlocalized, String localized) {

        return !localized.equals(unlocalized);
    }
}
