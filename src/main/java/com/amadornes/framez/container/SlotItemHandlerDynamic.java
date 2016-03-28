package com.amadornes.framez.container;

import com.amadornes.framez.api.DynamicReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SlotItemHandlerDynamic extends Slot {

    private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
    public final DynamicReference<? extends IItemHandler> itemHandler;
    private final int index;

    public SlotItemHandlerDynamic(DynamicReference<? extends IItemHandler> itemHandler, int index, int xPosition, int yPosition) {
        super(emptyInventory, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {

        if (stack == null) return false;
        ItemStack remainder = this.itemHandler.get().insertItem(index, stack, true);
        return remainder == null || remainder.stackSize < stack.stackSize;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    @Override
    public ItemStack getStack() {

        return this.itemHandler.get().getStackInSlot(index);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    /**
     * Helper method to put a stack in the slot.
     */
    @Override
    public void putStack(ItemStack stack) {

        ((IItemHandlerModifiable) this.itemHandler.get()).setStackInSlot(index, stack);
        this.onSlotChanged();
    }

    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {

    }

    @Override
    public int getItemStackLimit(ItemStack stack) {

        ItemStack maxAdd = stack.copy();
        maxAdd.stackSize = maxAdd.getMaxStackSize();
        ItemStack currentStack = this.itemHandler.get().getStackInSlot(index);
        ItemStack remainder = this.itemHandler.get().insertItem(index, maxAdd, true);

        int current = currentStack == null ? 0 : currentStack.stackSize;
        int added = maxAdd.stackSize - (remainder != null ? remainder.stackSize : 0);
        return current + added;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {

        return this.itemHandler.get().extractItem(index, 1, true) != null;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    @Override
    public ItemStack decrStackSize(int amount) {

        return this.itemHandler.get().extractItem(index, amount, false);
    }
}