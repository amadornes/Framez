package com.amadornes.framez.motor.logic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amadornes.blockdata.BlockData;
import com.amadornes.blockdata.BlockRotation;
import com.amadornes.blockdata.RotationAngle;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.motor.MotorTriggerRedstone;
import com.amadornes.framez.movement.IMovement;
import com.amadornes.framez.movement.MovementRotation;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;

public class MotorLogicRotator implements IMotorLogic {

    private DynamicReference<TileMotor> motor;
    private EnumFacing face = EnumFacing.DOWN;
    private AxisDirection direction = AxisDirection.NEGATIVE;
    private EnumMotorAction action = EnumMotorAction.STOP;

    public MotorLogicRotator() {

    }

    @Override
    public void initTriggers(Map<IMotorAction, MotorTrigger> triggers, List<IMotorAction> actionIdMap) {

        triggers.put(EnumMotorAction.ROTATE_CLOCKWISE, new MotorTrigger(new MotorTriggerRedstone(motor), false));
        triggers.put(EnumMotorAction.ROTATE_CCLOCKWISE, new MotorTrigger());
        triggers.put(EnumMotorAction.STOP, new MotorTrigger(new MotorTriggerRedstone(motor), true));

        actionIdMap.add(EnumMotorAction.ROTATE_CLOCKWISE);
        actionIdMap.add(EnumMotorAction.ROTATE_CCLOCKWISE);
        actionIdMap.add(EnumMotorAction.STOP);
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

        return motor.get().getMotorPos();
    }

    @Override
    public boolean rotate(EnumFacing axis) {

        if (axis.getAxis() == face.getAxis()) {
            direction = direction == AxisDirection.NEGATIVE ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
            return true;
        }
        for (int i = 0; i < (axis.getAxisDirection() == AxisDirection.POSITIVE ? 1 : 3); i++)
            face = face.rotateAround(axis.getAxis());
        return true;
    }

    @Override
    public double getConsumedEnergy(MovingStructure structure, double energyApplied) {

        System.out.println(energyApplied);
        return 0; // TODO: Determine consumed energy
    }

    @Override
    public boolean canMove(MovingStructure structure, IMotorAction action) {

        return true;
    }

    @Override
    public void move(MovingStructure structure, IMotorAction action, int duration) {

        this.action = (EnumMotorAction) action;
        IMotorLogic.super.move(structure, action, duration);
    }

    @Override
    public BlockData transform(MovingStructure structure, IMotorAction action, BlockData data) {

        return data.withRotation(new BlockRotation(face.getAxis(),
                action == EnumMotorAction.ROTATE_CCLOCKWISE ? RotationAngle.CCW_90 : RotationAngle.CW_90));
    }

    @Override
    public void onMovementComplete() {

    }

    @Override
    public IMovement getMovement(Set<MovingBlock> blocks, IMotorAction action) {

        return new MovementRotation(motor, face.getAxis(), (EnumMotorAction) action);
    }

    @Override
    public NBTTagCompound serializeNBT() {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("face", face.ordinal());
        tag.setInteger("direction", direction.ordinal());
        tag.setInteger("action", action.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {

        face = EnumFacing.getFront(tag.getInteger("face"));
        direction = EnumFacing.AxisDirection.values()[tag.getInteger("direction")];
        action = EnumMotorAction.VALUES[tag.getInteger("action")];
    }

    public EnumMotorAction getAction() {

        return action;
    }

}
