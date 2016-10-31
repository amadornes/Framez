package com.amadornes.framez.motor.logic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.EnumMotorAction;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.motor.MotorTriggerRedstone;
import com.amadornes.framez.movement.IMovement;
import com.amadornes.framez.movement.MovementTranslation;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MotorLogicBlinkDrive implements IMotorLogic<EnumFacing> {

    private DynamicReference<TileMotor> motor, linked;
    private EnumFacing face = EnumFacing.DOWN;
    private EnumMotorAction action = EnumMotorAction.MOVE_BACKWARD;

    public MotorLogicBlinkDrive(EnumFacing facing) {

        face = facing;
    }

    public MotorLogicBlinkDrive() {

    }

    @Override
    public void initTriggers(Map<IMotorAction, MotorTrigger> triggers, List<IMotorAction> actionIdMap) {

        triggers.put(EnumMotorAction.MOVE_FORWARD, new MotorTrigger(new MotorTriggerRedstone(motor), false));

        actionIdMap.add(EnumMotorAction.MOVE_FORWARD);
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
        for (int i = 0; i < (axis.getAxisDirection() == AxisDirection.POSITIVE ? 1 : 3); i++) {
            face = face.rotateAround(axis.getAxis());
        }
        if (face != oldFace) {
            updateLinked();
            return true;
        }
        return false;
    }

    @Override
    public boolean canMove(MovingStructure structure, IMotorAction action) {

        return linked != null;
    }

    @Override
    public boolean canMove() {

        return linked != null;
    }

    @Override
    public void move(MovingStructure structure, IMotorAction action, int duration) {

        this.action = (EnumMotorAction) action;
        IMotorLogic.super.move(structure, action, duration);
    }

    @Override
    public IMovement getMovement(Set<MovingBlock> blocks, IMotorAction action) {

        Axis axis = face.getAxis();
        BlockPos motorPos = getMotor().getMotorPos(), linkedPos = linked.get().getMotorPos();
        int v1 = axis == Axis.X ? motorPos.getX() : axis == Axis.Y ? motorPos.getY() : motorPos.getZ();
        int v2 = axis == Axis.X ? linkedPos.getX() : axis == Axis.Y ? linkedPos.getY() : linkedPos.getZ();
        int dist = MovingStructure.getSizeAlongAxis(blocks, axis, motorPos, Math.min(v1, v2), Math.max(v1, v2));
        return new MovementTranslation(action == EnumMotorAction.MOVE_BACKWARD ? face.getOpposite() : face, dist);
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

    public void updateLinked() {

        World world = getMotor().getMotorWorld();
        if (!world.isRemote) {
            return;
        }

        BlockPos pos = getMotor().getMotorPos();
        EnumFacing otherFacing = face.getOpposite();
        DynamicReference<TileMotor> prev = linked;

        for (int i = 0; i < FramezConfig.Motor.blinkDriveRange; i++) {
            pos = pos.offset(face);
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileMotor) {
                IMotorLogic<?> logic = ((TileMotor) tile).getLogic();
                if (logic instanceof MotorLogicBlinkDrive && logic.getFace() == otherFacing) {
                    linked = ((MotorLogicBlinkDrive) logic).motor;
                    ((MotorLogicBlinkDrive) logic).linked = motor;
                    getMotor().sendUpdatePacket();
                    logic.getMotor().sendUpdatePacket();
                }
            }
        }

        if (linked != prev && prev != null) {
            ((MotorLogicBlinkDrive) linked.get().getLogic()).linked = null;
            ((MotorLogicBlinkDrive) linked.get().getLogic()).updateLinked();
        }
    }

    @Override
    public void onFirstTick() {

        updateLinked();
    }

    @Override
    public void invalidate() {

        if (linked != null) {
            ((MotorLogicBlinkDrive) linked.get().getLogic()).linked = null;
            linked.get().sendUpdatePacket();
            linked = null;
            getMotor().sendUpdatePacket();
        }
    }

}
