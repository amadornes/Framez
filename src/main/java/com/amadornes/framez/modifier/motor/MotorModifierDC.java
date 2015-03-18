package com.amadornes.framez.modifier.motor;

import java.util.Collection;

import com.amadornes.framez.api.modifier.IMotorModifier;
import com.amadornes.framez.api.modifier.IMotorModifierPower;
import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.jtraits.ITrait;
import com.amadornes.jtraits.JTrait;

public class MotorModifierDC implements IMotorModifierPower {

    @Override
    public String getType() {

        return "dc";
    }

    @Override
    public boolean isCompatibleWith(IMotorModifier mod) {

        return !(mod instanceof IMotorModifierPower);
    }

    @Override
    public boolean isValidCombination(Collection<IMotorModifier> combination) {

        return true;
    }

    @Override
    public Class<? extends ITrait> getTraitClass() {

        return TMotorDC.class;
    }

    public static abstract class TMotorDC extends JTrait<IMotor> implements IMotor {

        @Override
        public double drainPower(double amount, boolean simulated) {

            return amount;
        }

    }

}
