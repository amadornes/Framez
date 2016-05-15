package com.amadornes.framez.upgrade;

import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.modifier.IMotorUpgrade.IMotorUpgradeData;

public class MotorUpgradeData implements IMotorUpgradeData {

    private IMotorUpgrade upgrade;
    private ItemStack stack;
    private Object data;

    public MotorUpgradeData(IMotorUpgrade upgrade, ItemStack stack, Object data) {

        this.upgrade = upgrade;
        this.stack = stack;
        this.data = data;
    }

    @Override
    public IMotorUpgrade getUpgrade() {

        return upgrade;
    }

    @Override
    public ItemStack getStack() {

        return stack;
    }

    @Override
    public Object getData() {

        return data;
    }

}
