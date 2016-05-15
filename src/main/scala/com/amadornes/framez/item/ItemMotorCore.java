package com.amadornes.framez.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.init.FramezCreativeTab;
import com.amadornes.framez.ref.ModInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMotorCore extends ItemFramez {

    public ItemMotorCore() {

        super("motor_core");

        setCreativeTab(FramezCreativeTab.tab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        String type = "Frame Motor Core";
        if (stack.getItemDamage() == 1)
            type = "Frame Slider Core";
        else if (stack.getItemDamage() == 2)
            type = "Frame Rotator";
        else if (stack.getItemDamage() == 3)
            type = "Frame Linear Actuator Core";
        else if (stack.getItemDamage() == 4)
            type = "Frame Blink Drive Core";
        return type;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        String type = null;
        if (stack.getItemDamage() == 1)
            type = "slider";
        else if (stack.getItemDamage() == 2)
            type = "rotator";
        else if (stack.getItemDamage() == 3)
            type = "linearactuator";
        else if (stack.getItemDamage() == 4)
            type = "blinkdrive";

        return StatCollector.translateToLocalFormatted("item." + ModInfo.MODID + ":motorcore" + (type == null ? "" : "_" + type) + ".name");
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {

        l.add(new ItemStack(item, 1, 0));
        if (FramezConfig.enable_frame_sliders)
            l.add(new ItemStack(item, 1, 1));
        if (FramezConfig.enable_frame_rotators)
            l.add(new ItemStack(item, 1, 2));
        if (FramezConfig.enable_frame_linear_actuators)
            l.add(new ItemStack(item, 1, 3));
        if (FramezConfig.enable_frame_blink_drives)
            l.add(new ItemStack(item, 1, 4));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

    }

}
