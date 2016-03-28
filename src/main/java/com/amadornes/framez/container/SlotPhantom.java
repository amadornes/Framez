package com.amadornes.framez.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

// TODO: Look into this... Srsly
public class SlotPhantom extends SlotItemHandler {

    public SlotPhantom(IItemHandler itemHandler, int index, int xPosition, int yPosition) {

        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {

        return 0;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {

        super.onPickupFromSlot(player, stack);
        player.inventory.setItemStack(null);
    }

    @Override
    public void putStack(ItemStack stack) {

        super.putStack(stack.copy().splitStack(1));
    }

    @Override
    public ItemStack getStack() {

        return super.getStack();
    }

}