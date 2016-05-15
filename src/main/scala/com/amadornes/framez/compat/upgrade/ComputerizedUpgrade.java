package com.amadornes.framez.compat.upgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.movement.IMotor;

public class ComputerizedUpgrade implements IMotorUpgrade {

    public static final ComputerizedUpgrade upgrade = new ComputerizedUpgrade();
    public final List<ItemStack> items = new ArrayList<ItemStack>();

    @Override
    public String getType() {

        return "computer";
    }

    @Override
    public boolean isUpgradeStack(ItemStack stack) {

        for (ItemStack is : items)
            if (stack.isItemEqual(is))
                return true;

        return false;
    }

    @Override
    public boolean canApply(IMotor motor, ItemStack stack, EntityPlayer player) {

        return true;
    }

    @Override
    public boolean canApplyDirectly(IMotor motor, ItemStack stack, EntityPlayer player) {

        return false;
    }

}
