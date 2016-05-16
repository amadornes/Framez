package com.amadornes.framez.motor.logic;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.amadornes.blockdata.BlockData;
import com.amadornes.blockdata.BlockRotation;
import com.amadornes.blockdata.RotationAngle;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.motor.MotorTriggerConstant;
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
    public IMotorAction initTriggers(Map<IMotorAction, MotorTrigger> triggers, List<IMotorAction> actionIdMap) {

        triggers.put(EnumMotorAction.MOVE_FORWARD, new MotorTrigger(new MotorTriggerRedstone(motor), false));
        triggers.put(EnumMotorAction.MOVE_BACKWARD, new MotorTrigger(new MotorTriggerConstant(), true));
        triggers.put(EnumMotorAction.STOP, new MotorTrigger(new MotorTriggerRedstone(motor), true));

        actionIdMap.add(EnumMotorAction.MOVE_FORWARD);
        actionIdMap.add(EnumMotorAction.MOVE_BACKWARD);
        actionIdMap.add(EnumMotorAction.STOP);

        return EnumMotorAction.STOP;
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

        return 0; // TODO: Determine consumed energy
    }

    @Override
    public boolean canMove(MovingStructure structure, IMotorAction action) {

        return true;
    }

    @Override
    public void move(MovingStructure structure, IMotorAction action) {

        this.action = (EnumMotorAction) action;
        for (Entry<MovingBlock, BlockPos> block : structure.getBlocks().entrySet()) {
            BlockData data = block.getKey().toBlockData();
            data.remove(block.getKey().getWorld(), block.getKey().getPos(), 3);
            data = BlockData.fromNBT(data.serializeNBT());// TODO: Make it so this isn't required?
            data = data.withRotation(
                    new BlockRotation(face.getAxis(), direction == AxisDirection.POSITIVE ? RotationAngle.CCW_90 : RotationAngle.CW_90));
            data.place(block.getKey().getWorld(), block.getValue(), 3);
        }
    }

    @Override
    public void onMovementComplete() {

    }

    @Override
    public IMovement getMovement(Set<MovingBlock> blocks, IMotorAction action) {

        return new MovementRotation();
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
