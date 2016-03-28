package com.amadornes.framez.item;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.framez.ModInfo;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.framez.motor.MotorRegistry;
import com.amadornes.framez.util.ICapabiltyLambda;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemMotorUpgrade extends Item {

    public ItemMotorUpgrade() {

        setUnlocalizedName(ModInfo.MODID + ":motor_upgrade");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return super.getUnlocalizedName(stack) + "."
                + new ArrayList<String>(MotorRegistry.INSTANCE.internalUpgrades.keySet()).get(stack.getItemDamage()).toLowerCase();
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

        int i = 0;
        for (String type : MotorRegistry.INSTANCE.internalUpgrades.keySet())
            subItems.add(new ItemStack(itemIn, 1, i++));
    }

    @Override
    public ICapabiltyLambda initCapabilities(ItemStack stack, NBTTagCompound nbt) {

        return (c, f) -> {
            if (f != null || c != IMotorUpgradeFactory.CAPABILITY_ITEM_UPGRADE) return null;
            String t = null;
            int i = 0;
            for (String type : MotorRegistry.INSTANCE.internalUpgrades.keySet()) {
                t = type;
                if (i++ == stack.getItemDamage()) break;
            }
            return MotorRegistry.INSTANCE.internalUpgrades.get(t);
        };
    }

}
