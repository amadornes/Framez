package com.amadornes.framez.motor.logic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.motor.MotorTriggerRedstone;
import com.amadornes.framez.movement.IMovement;
import com.amadornes.framez.movement.MovementTranslation;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;

public class MotorLogicLinearActuator implements IMotorLogic {

    private DynamicReference<TileMotor> motor;
    private EnumFacing face = EnumFacing.DOWN;
    private EnumMotorAction action = EnumMotorAction.MOVE_BACKWARD;

    public MotorLogicLinearActuator() {

    }

    @Override
    public IMotorAction initTriggers(Map<IMotorAction, MotorTrigger> triggers, List<IMotorAction> actionIdMap) {

        triggers.put(EnumMotorAction.MOVE_FORWARD, new MotorTrigger(new MotorTriggerRedstone(motor), false));
        triggers.put(EnumMotorAction.MOVE_BACKWARD, new MotorTrigger(new MotorTriggerRedstone(motor), true));

        actionIdMap.add(EnumMotorAction.MOVE_FORWARD);
        actionIdMap.add(EnumMotorAction.MOVE_BACKWARD);

        return EnumMotorAction.MOVE_BACKWARD;
    }

    @Override
    public EnumFacing getFace() {

        return face;
    }

    @Override
    public TileMotor getMotor() {

        return motor.get();
    }

    @Override
    public void setMotor(DynamicReference<TileMotor> motor) {

        this.motor = motor;
    }

    @Override
    public BlockPos getStructureSearchLocation(IMotorAction action) {

        return action == EnumMotorAction.MOVE_BACKWARD ? motor.get().getMotorPos().offset(getFace()) : motor.get().getMotorPos();
    }

    @Override
    public boolean rotate(EnumFacing axis) {

        EnumFacing oldFace = face;
        for (int i = 0; i < (axis.getAxisDirection() == AxisDirection.POSITIVE ? 1 : 3); i++)
            face = face.rotateAround(axis.getAxis());
        return face != oldFace;
    }

    @Override
    public double getConsumedEnergy(MovingStructure structure, double energyApplied) {

        return 0; // TODO: Determine consumed energy
    }

    @Override
    public boolean canMove(MovingStructure structure, IMotorAction action) {

        return action != this.action;
    }

    @Override
    public void move(MovingStructure structure, IMotorAction action) {

        this.action = (EnumMotorAction) action;
        IMotorLogic.super.move(structure, action);
    }

    @Override
    public void onMovementComplete() {

    }

    @Override
    public IMovement getMovement(Set<MovingBlock> blocks, IMotorAction action) {

        return new MovementTranslation(action == EnumMotorAction.MOVE_BACKWARD ? face.getOpposite() : face);
    }

    @Override
    public NBTTagCompound serializeNBT() {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("face", face.ordinal());
        tag.setInteger("action", action.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {

        face = EnumFacing.getFront(tag.getInteger("face"));
        action = EnumMotorAction.VALUES[tag.getInteger("action")];
    }

    public EnumMotorAction getAction() {

        return action;
    }

}
