package com.amadornes.framez.motor.logic;

import java.util.Map.Entry;
import java.util.Set;

import com.amadornes.blockdata.BlockData;
import com.amadornes.blockdata.BlockRotation;
import com.amadornes.blockdata.RotationAngle;
import com.amadornes.framez.api.DynamicReference;
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
    public boolean canMove(MovingStructure structure) {

        return true;
    }

    @Override
    public void move(MovingStructure structure) {

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
    public IMovement getMovement(Set<MovingBlock> blocks) {

        return new MovementRotation();
    }

    @Override
    public NBTTagCompound serializeNBT() {

        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

}
