package com.amadornes.framez.item;

import java.util.List;

import com.amadornes.framez.Framez;
import com.amadornes.framez.ModInfo;
import com.amadornes.framez.util.FramezUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFramezItem {

    public String getName();

    public boolean isBlock();

    public default String getUnlocalizedName(ItemStack stack) {

        return (isBlock() ? "tile." : "item.") + ModInfo.MODID + ":" + getName();
    }

    public default String getUnlocalizedTip(ItemStack stack, EntityPlayer player, boolean advanced) {

        return getUnlocalizedName(stack).replace("item.", "tooltip.");
    }

    @SideOnly(Side.CLIENT)
    public default void addTipInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, String tip, boolean advanced) {

        tooltip.addAll(FramezUtils.split(tip, 35));
    }

    public default void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {

        String unlocalized = getUnlocalizedTip(stack, player, advanced);
        String localized = I18n.format(unlocalized);
        if (!hasTooltip(stack, player, unlocalized, localized, advanced)) return;

        if (!Framez.proxy.isShiftDown()) {
            tooltip.add(I18n.format("tooltip." + ModInfo.MODID + ":shift", EnumChatFormatting.GRAY,
                    EnumChatFormatting.YELLOW + "" + EnumChatFormatting.ITALIC, EnumChatFormatting.RESET + "" + EnumChatFormatting.GRAY));
        } else {
            addTipInformation(stack, player, tooltip, localized, advanced);
        }
    }

    public default boolean hasTooltip(ItemStack stack, EntityPlayer player, String unlocalized, String localized, boolean advanced) {

        return !localized.equals(unlocalized);
    }

}
