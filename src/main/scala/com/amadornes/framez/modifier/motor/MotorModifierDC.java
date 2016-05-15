package com.amadornes.framez.modifier.motor;

import java.util.Collection;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.jtraits.JTrait;

import cpw.mods.fml.common.registry.GameRegistry;

public class MotorModifierDC implements IMotorModifierPower {

    @Override
    public String getType() {

        return "dc";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier modifier) {

        return !(modifier instanceof IMotorModifierPower);
    }

    @Override
    public boolean isCombinationValid(Collection<IMotorModifier> modifiers) {

        return true;
    }

    @Override
    public Class<? extends JTrait<? extends IMotor>> getTraitClass() {

        return TMotorDC.class;
    }

    @Override
    public void registerRecipes(ItemStack unmodified, ItemStack modified) {

        if (FramezConfig.craftable_dc_motors)
            GameRegistry.addShapedRecipe(modified, "rrr", "rur", "rrr", 'r', Blocks.redstone_block, 'u', unmodified);
    }

    public static class TMotorDC extends JTrait<IMotor> {

        public double drainPower(double amount, boolean simulated) {

            return amount;
        }
    }

}
