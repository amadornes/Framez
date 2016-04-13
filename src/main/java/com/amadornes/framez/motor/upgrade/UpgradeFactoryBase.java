package com.amadornes.framez.motor.upgrade;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.api.motor.IMotorInteractions;
import com.amadornes.framez.api.motor.IMotorUpgrade;
import com.amadornes.framez.api.motor.IMotorUpgradeFactory;
import com.amadornes.jtraits.JTrait;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class UpgradeFactoryBase implements IMotorUpgradeFactory {

    private String type;
    private IMotorUpgradeCreatorInt creator;
    private Class<? extends JTrait<? extends IMotorInteractions>> trait;

    private ItemStack stack;

    public UpgradeFactoryBase(String type, IMotorUpgradeCreatorInt creator, Class<? extends JTrait<? extends IMotorInteractions>> trait) {

        this.type = type;
        this.creator = creator;
        this.trait = trait;
    }

    public UpgradeFactoryBase(String name, IMotorUpgradeCreatorInt creator) {

        this(name, creator, null);
    }

    public void setStack(ItemStack stack) {

        this.stack = stack;
    }

    @Override
    public String getType() {

        return type;
    }

    @Override
    public List<ItemStack> getCraftingItems() {

        return Arrays.asList(stack);
    }

    @Override
    public boolean isUpgradeStack(ItemStack stack) {

        return ItemStack.areItemsEqual(stack, this.stack) && ItemStack.areItemStackTagsEqual(stack, this.stack);
    }

    @Override
    public boolean canApply(DynamicReference<? extends IMotor> motor, ItemStack stack, EntityPlayer player) {

        for (int i = 0; i < motor.get().getUpgradeSlots(); i++) {
            Entry<IMotorUpgrade, ItemStack> upgrade = motor.get().getUpgrade(i);
            if (upgrade != null && upgrade.getKey().getType().equals(getType())) return false;
        }
        return true;
    }

    @Override
    public boolean canApplyDirectly(DynamicReference<? extends IMotor> motor, ItemStack stack, EntityPlayer player) {

        return true;
    }

    @Override
    public IMotorUpgrade createUpgrade(DynamicReference<? extends IMotor> motor, int slot) {

        return creator.createUpgrade(motor, slot);
    }

    @Override
    public Class<? extends JTrait<? extends IMotorInteractions>> getTrait() {

        return trait;
    }

    public static interface IMotorUpgradeCreatorInt {

        public IMotorUpgrade createUpgrade(DynamicReference<? extends IMotor> motor, int slot);

    }

}
