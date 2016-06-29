package com.amadornes.framez.item;

import java.util.List;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.motor.logic.MotorLogicType;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlockMotor extends ItemBlockFramez {

    public ItemBlockMotor(Block block) {

        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        int meta = stack.getItemDamage();
        if (meta >= MotorLogicType.VALUES.length) return "<ERROR>";
        return "tile." + ModInfo.MODID + ":motor." + MotorLogicType.VALUES[meta].name().toLowerCase();
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> items) {

        for (int i = 0; i < MotorLogicType.VALUES.length; i++)
            items.add(new ItemStack(item, 1, i));
    }

    @Override
    public int getMetadata(int itemMeta) {

        return itemMeta;
    }

}