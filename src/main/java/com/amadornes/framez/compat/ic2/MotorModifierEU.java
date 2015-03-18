package com.amadornes.framez.compat.ic2;

import ic2.api.energy.tile.IEnergySink;

import java.util.Collection;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

public class MotorModifierEU implements IMotorModifierPower {

    @Override
    public String getType() {

        return "eu";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return true;
    }

    @Override
    public boolean isValidCombination(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TMotorEU.class;
    }

    public static abstract class TMotorEU extends JTrait<IMotor> implements IMotor, IEnergySink {

        @Override
        public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {

            return true;
        }

        @Override
        public double getDemandedEnergy() {

            return ((IMotor) this).getEnergyBufferSize() - ((IMotor) this).getEnergyBuffer();
        }

        @Override
        public int getSinkTier() {

            return 4;
        }

        @Override
        public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {

            return amount - ((IMotor) this).injectPower(amount, false);
        }

    }

}
