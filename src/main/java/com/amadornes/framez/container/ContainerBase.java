package com.amadornes.framez.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {

        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 9) {
                if (!this.mergeItemStack(itemstack1, 9, inventorySlots.size(), true)) { return null; }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) { return null; }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) { return null; }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {

        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (slot.isItemValid(stack)) {
                    if (itemstack != null && itemstack.getItem() == stack.getItem()
                            && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata())
                            && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                        int j = itemstack.stackSize + stack.stackSize;

                        if (j <= stack.getMaxStackSize()) {
                            stack.stackSize = 0;
                            itemstack.stackSize = j;
                            slot.onSlotChanged();
                            flag = true;
                        } else if (itemstack.stackSize < stack.getMaxStackSize()) {
                            stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
                            itemstack.stackSize = stack.getMaxStackSize();
                            slot.onSlotChanged();
                            flag = true;
                        }
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                // Forge: Make sure to respect isItemValid in the slot.
                if (itemstack1 == null && slot1.isItemValid(stack)) {
                    slot1.putStack(stack.copy());
                    slot1.onSlotChanged();
                    stack.stackSize = 0;
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

}
