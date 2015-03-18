package com.amadornes.framez.compat.rf;

import java.util.Collection;

import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

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
    public boolean isValidCombination(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TMotorRF.class;
    }

    public static abstract class TMotorRF extends JTrait<IMotor> implements IMotor, IEnergyHandler, IEnergyReceiver {

        @Override
        public boolean canConnectEnergy(ForgeDirection from) {

            return true;
        }

        @Override
        public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

            return (int) ((IMotor) this).injectPower(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

            return 0;
        }

        @Override
        public int getEnergyStored(ForgeDirection from) {

            return (int) ((IMotor) this).getEnergyBuffer();
        }

        @Override
        public int getMaxEnergyStored(ForgeDirection from) {

            return (int) ((IMotor) this).getEnergyBufferSize();
        }

    }

}
