package com.amadornes.framez.container;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.motor.upgrade.UpgradeCamouflage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCamouflage extends ContainerBase {

    private final DynamicReference<? extends IMotor> motor;

    public ContainerCamouflage(DynamicReference<? extends IMotor> motor, UpgradeCamouflage upgrade, IInventory playerInventory) {

        this.motor = motor;

        int i = 8;
        int j = 156;

        for (int k = 0; k < 6; k++) {
            this.addSlotToContainer(new SlotItemHandler(upgrade, k, 8, 12 + k * 24));
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, i + x * 18, j + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInventory, x, i + x * 18, 58 + j));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return motor.get().getMotorPos() != null && player.getPosition().distanceSq(motor.get().getMotorPos()) < 64.0D;
    }

}
