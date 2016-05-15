package com.amadornes.framez.upgrade;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.api.modifier.IMotorUpgrade.IConfigurableMotorUpgrade;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.client.gui.upgrade.GuiUpgradeCamouflage;
import com.amadornes.framez.init.FramezItems;
import com.amadornes.framez.upgrade.MotorUpgradeCamouflage.MotorUpgradeCamouflageData;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MotorUpgradeCamouflage implements IConfigurableMotorUpgrade<MotorUpgradeCamouflageData> {

    @Override
    public String getType() {

        return "camouflage";
    }

    @Override
    public boolean isUpgradeStack(ItemStack stack) {

        return stack.getItem() == FramezItems.upgrade_camouflage;
    }

    @Override
    public boolean canApply(IMotor motor, ItemStack stack, EntityPlayer player) {

        for (int i = 0; i < 7; i++) {
            IMotorUpgradeData data = motor.getUpgrades()[i];
            if (data != null && data.getUpgrade() == this)
                return false;
        }

        return true;
    }

    @Override
    public boolean canApplyDirectly(IMotor motor, ItemStack stack, EntityPlayer player) {

        return canApply(motor, stack, player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getConfigGUI(IMotor motor, int slot, GuiScreen parent) {

        return new GuiUpgradeCamouflage(motor, slot, parent);
    }

    @Override
    public MotorUpgradeCamouflageData createUpgradeData(IMotor motor) {

        return new MotorUpgradeCamouflageData(motor);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, MotorUpgradeCamouflageData data) {

        for (int i = 0; i < 6; i++) {
            if (data.getCamo(i) == null)
                continue;
            NBTTagCompound item = new NBTTagCompound();
            data.getCamo(i).writeToNBT(item);
            tag.setTag("item_" + i, item);
            tag.setInteger("face_" + i, data.getCamoFace(i));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, MotorUpgradeCamouflageData data) {

        MotorUpgradeCamouflageData d = data;

        for (int i = 0; i < 6; i++) {
            if (!tag.hasKey("item_" + i)) {
                d.setCamo(i, null, i);
            } else {
                d.setCamo(i, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item_" + i)), tag.getInteger("face_" + i));
            }
        }
    }

    public static class MotorUpgradeCamouflageData {

        private IMotor motor;
        private ItemStack[] camo = new ItemStack[6];
        private int[] camoFace = new int[] { 0, 1, 2, 3, 4, 5 };

        public MotorUpgradeCamouflageData(IMotor motor) {

            this.motor = motor;
        }

        public void setCamo(int side, ItemStack camo, int face) {

            if (camo == null) {
                this.camo[side] = null;
            } else {
                this.camo[side] = camo.copy();
                this.camo[side].stackSize = 1;
            }
            this.camoFace[side] = face;

            motor.notifyChange();
        }

        public ItemStack getCamo(int side) {

            return this.camo[side];
        }

        public int getCamoFace(int side) {

            return this.camoFace[side];
        }

    }

}
