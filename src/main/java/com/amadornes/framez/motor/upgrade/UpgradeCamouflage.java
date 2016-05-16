package com.amadornes.framez.motor.upgrade;

import com.amadornes.framez.Framez;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotor;
import com.amadornes.framez.client.gui.GuiCamouflage;
import com.amadornes.framez.container.ContainerCamouflage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class UpgradeCamouflage extends UpgradeBase implements IItemHandlerModifiable {

    private ItemStack[] slots = new ItemStack[6];

    public UpgradeCamouflage(DynamicReference<? extends IMotor> motor, int slot) {

        super(motor, slot);
    }

    @Override
    public boolean hasConfigGUI() {

        return true;
    }

    @Override
    public Container getGuiContainer(EntityPlayer player) {

        return new ContainerCamouflage(motor, this, player.inventory);
    }

    @Override
    public GuiScreen getConfigGUI(EntityPlayer player, GuiScreen parent) {

        return new GuiCamouflage(parent, this, motor);
    }

    @Override
    public int getSlots() {

        return 6;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return slots[slot];
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

        if (slots[slot] != null) return stack;
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            try {
                IBlockState faceState = ((ItemBlock) stack.getItem()).block
                        .getStateFromMeta(((ItemBlock) stack.getItem()).getMetadata(stack.getMetadata()));
                if (Framez.proxy.isFullBlock(faceState)) {
                    slots[slot] = stack.copy().splitStack(1);
                    return stack.copy().splitStack(stack.stackSize - 1);
                }
            } catch (Exception ex) {
            }
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

        if (!simulate) {
            ItemStack stack = slots[slot];
            slots[slot] = null;
            return stack;
        }
        return slots[slot];
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {

        slots[slot] = stack;
    }

}
