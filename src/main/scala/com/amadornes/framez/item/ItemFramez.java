package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import com.amadornes.framez.ref.ModInfo;
import com.amadornes.framez.util.FramezUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFramez extends Item {

    private String name = "error";

    public ItemFramez() {

    }

    public ItemFramez(String name) {

        this.name = name;
        setTextureName(ModInfo.MODID + ":" + name);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "item." + ModInfo.MODID + ":" + name;
    }

    protected String getUnlocalizedTip(ItemStack stack, EntityPlayer player) {

        return getUnlocalizedName(stack).replace("item.", "tooltip.");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    protected void addTipInformation(ItemStack stack, EntityPlayer player, List l, String tip) {

        l.addAll(FramezUtils.split(tip, 35));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List l, boolean unused) {

        String unlocalized = getUnlocalizedTip(stack, player);
        String localized = I18n.format(unlocalized);
        if (!hasTooltip(stack, player, unlocalized, localized))
            return;

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
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
