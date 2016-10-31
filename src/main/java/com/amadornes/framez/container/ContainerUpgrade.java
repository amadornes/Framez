package com.amadornes.framez.container;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerUpgrade extends ContainerBase {

    private final DynamicReference<TileMotor> motor;

    public ContainerUpgrade(DynamicReference<TileMotor> motor, int upgradeSlotID, IInventory playerInventory) {

        this.motor = motor;

        this.addSlotToContainer(new SlotItemHandlerDynamic(motor, upgradeSlotID, 79, 36));
        int i = 8;
        int j = 86;

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
