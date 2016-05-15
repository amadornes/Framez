package com.amadornes.framez.compat.rf;

import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.compat.Dependencies;
import com.amadornes.jtraits.JTrait;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class MotorModifierRF implements IMotorModifierPower {

    @Override
    public String getType() {

        return "rf";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return true;
    }

    @Override
    public boolean isCombinationValid(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public void registerRecipes(ItemStack unmodified, ItemStack modified) {

        if (Loader.isModLoaded(Dependencies.TF) && Loader.isModLoaded(Dependencies.TE)) {
            Item tf_material = GameRegistry.findItem(Dependencies.TF, "material");
            Item te_material = GameRegistry.findItem(Dependencies.TE, "material");
            Item cell = GameRegistry.findItem(Dependencies.TE, "Cell");

            if (tf_material != null && te_material != null)
                GameRegistry.addShapedRecipe(modified, "ici", "nun", "iki", 'i', new ItemStack(tf_material, 1, 72), 'c', new ItemStack(
                        cell, 1, 2), 'n', new ItemStack(tf_material, 1, 108), 'u', unmodified, 'k', new ItemStack(te_material, 1, 1));
        }

        if (Loader.isModLoaded(Dependencies.EIO)) {
            Item alloy = GameRegistry.findItem(Dependencies.EIO, "itemAlloy");
            Item material = GameRegistry.findItem(Dependencies.EIO, "itemMaterial");

            if (alloy != null && material != null)
                GameRegistry.addShapedRecipe(modified, "ici", "nun", "iai", 'i', new ItemStack(alloy, 1, 6), 'c', new ItemStack(material,
                        1, 6), 'n', new ItemStack(material, 1, 3), 'u', unmodified, 'a', new ItemStack(alloy, 1, 1));
        }
    }

    @Override
    public Class<? extends JTrait<? extends IMotor>> getTraitClass() {

        return TMotorRF.class;
    }

    public static class TMotorRF extends JTrait<IMotor> implements IEnergyHandler, IEnergyReceiver {

        @Override
        public boolean canConnectEnergy(ForgeDirection from) {

            return true;
        }

        @Override
        public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

            return 0;
        }

        @Override
        public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

            return (int) _super.injectPower(maxReceive, simulate);
        }

        @Override
        public int getEnergyStored(ForgeDirection from) {

            return (int) _super.getEnergyBuffer();
        }

        @Override
        public int getMaxEnergyStored(ForgeDirection from) {

            return (int) _super.getEnergyBufferSize();
        }

    }

}
