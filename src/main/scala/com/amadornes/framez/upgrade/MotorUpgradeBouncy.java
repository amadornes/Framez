package com.amadornes.framez.upgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.modifier.IMotorUpgrade;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.movement.MovementSlide;

public class MotorUpgradeBouncy implements IMotorUpgrade {

    @Override
    public String getType() {

        return "bouncy";
    }

    @Override
    public boolean isUpgradeStack(ItemStack stack) {

        return stack.getItem() == FramezItems.upgrade_bouncy;
    }

    @Override
    public boolean canApply(IMotor motor, ItemStack stack, EntityPlayer player) {

        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData data = motor.getUpgrades()[i];
            if (data != null && data.getUpgrade() == this)
                return false;
        }

        return motor.getMovement() instanceof MovementSlide;
    }

    @Override
    public boolean canApplyDirectly(IMotor motor, ItemStack stack, EntityPlayer player) {

        return canApply(motor, stack, player);
    }

}
