package com.amadornes.framez.api.movement;

import java.util.Collection;
import java.util.Set;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.amadornes.framez.api.modifier.IMotorModifier;

public interface IMotor extends IWorldLocation {

    public Collection<IMotorModifier> getModifiers();

    public boolean isWorking();

    public boolean canWork();

    public boolean move();

    public IMovement getMovement();

    public Set<MotorSetting> getSettings();

    public void configure(MotorSetting setting);

    public ForgeDirection getFace();

    public double getEnergyBufferSize();

    public double getEnergyBuffer();

    /**
     * Returns the amount of power that was injected
     */
    public double injectPower(double amount, boolean simulated);

    /**
     * Returns the amount of power that was drained
     */
    public double drainPower(double amount, boolean simulated);

}
