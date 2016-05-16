package com.amadornes.framez.api.motor;

import java.util.List;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.jtraits.JTrait;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IMotorUpgradeFactory {

    @CapabilityInject(IMotorUpgradeFactory.class)
    public static final Capability<IMotorUpgradeFactory> CAPABILITY_ITEM_UPGRADE = null;

    public List<ItemStack> getCraftingItems();

    public boolean isUpgradeStack(ItemStack stack);

    public boolean canApply(DynamicReference<? extends IMotor> motor, ItemStack stack, EntityPlayer player);

    public boolean canApplyDirectly(DynamicReference<? extends IMotor> motor, ItemStack stack, EntityPlayer player);

    public IMotorUpgrade createUpgrade(DynamicReference<? extends IMotor> motor, int slot);

    public Class<? extends JTrait<? extends IMotorInteractions>> getTrait();

}
