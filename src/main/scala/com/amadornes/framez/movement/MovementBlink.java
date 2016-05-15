package com.amadornes.framez.movement;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.framez.api.movement.IMotor;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovement.IMovementBlink;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.movement.TrajectoryTranslation;
import com.amadornes.trajectory.util.MiscUtils;

public class MovementBlink extends TrajectoryTranslation implements IMovementBlink {

    private int direction;
    private int distance;

    public MovementBlink(int direction, int distance, double speed) {

        super(BlockPos.sideOffsets[direction].copy().mul(distance), speed);
        this.distance = distance;
        this.direction = direction;
    }

    public MovementBlink() {

        super();
    }

    @Override
    public int getDirection() {

        return direction;
    }

    @Override
    public void setDirection(int dir) {

        direction = dir;
        translation = BlockPos.sideOffsets[dir].copy().mul(distance);
    }

    @Override
    public int getDistance() {

        return distance;
    }

    @Override
    public void setDistance(int distance) {

        this.distance = distance;
        translation = BlockPos.sideOffsets[getDirection()].copy().mul(distance);
    }

    @Override
    public void transformGL(Vector3 position, BlockSet blocks, float ticks) {

        super.transformGL(position, blocks, ticks);
        // double progress = Math.max(0, getProgress(ticks));
        // Vector3 center = new Vector3();
        // BlockPos min = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), max = new BlockPos(Integer.MIN_VALUE,
        // Integer.MIN_VALUE, Integer.MIN_VALUE);
        //
        // for (IMovingBlock b : blocks) {
        // center.add(b.getPosition().x, 0, b.getPosition().z);
        // min.x = Math.min(min.x, b.getPosition().x);
        // min.y = Math.min(min.y, b.getPosition().y);
        // min.z = Math.min(min.z, b.getPosition().z);
        // max.x = Math.max(max.x, b.getPosition().x);
        // max.y = Math.max(max.y, b.getPosition().y);
        // max.z = Math.max(max.z, b.getPosition().z);
        // }
        // center.div(blocks.size());
        // center.sub(position);
        // center.y = 0;
        // center.add(Vector3.center);
        //
        // double blockSize = 1D / (max.getSide(direction) - min.getSide(direction) + 1);
        // double scaleTime = 0.25;
        //
        // if (progress < scaleTime) {
        // double s = blockSize + (1 - blockSize) * (1 - (progress / scaleTime));
        //
        // GL11.glTranslated(center.x, center.y, center.z);
        // GL11.glScaled(s, s, s);
        // GL11.glTranslated(-center.x, -center.y, -center.z);
        // } else if (progress > 1 - scaleTime) {
        // BlockPos translation = getTranslation();
        // GL11.glTranslated(translation.x, translation.y, translation.z);
        //
        // double s = blockSize + (1 - blockSize) * (1 - ((1 - progress) / scaleTime));
        //
        // GL11.glTranslated(center.x, center.y, center.z);
        // GL11.glScaled(s, s, s);
        // GL11.glTranslated(-center.x, -center.y, -center.z);
        // } else {
        // double p = (progress - scaleTime) / (1 - scaleTime * 2);
        // BlockPos translation = getTranslation();
        // GL11.glTranslated(translation.x * p, translation.y * p, translation.z * p);
        //
        // GL11.glTranslated(center.x, center.y, center.z);
        // GL11.glScaled(blockSize, blockSize, blockSize);
        // GL11.glTranslated(-center.x, -center.y, -center.z);
        // }
    }

    @Override
    public void moveEntity(Entity entity, float ticks) {

        super.moveEntity(entity, ticks);
    }

    @Override
    public MovementBlink copy() {

        return new MovementBlink(getDirection(), getDistance(), getSpeed());
    }

    @Override
    public IMovement withSpeed(double speed) {

        return new MovementBlink(getDirection(), getDistance(), speed);
    }

    @Override
    public boolean rotate(int axis, IMotor motor) {

        int f = MiscUtils.rotate(motor.getFace(), axis);
        motor.setFace(f);
        setDirection(f);

        return true;
    }

    @Override
    public double getSpeedMultiplier(BlockSet blocks) {

        return 1;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("direction", direction);
        tag.setInteger("distance", distance);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        direction = tag.getInteger("direction");
        distance = tag.getInteger("distance");
    }

    @Override
    public Map<BlockPos, Boolean> getPlaceholderPositions(BlockSet blocks) {

        Map<BlockPos, Boolean> placeholder = new HashMap<BlockPos, Boolean>();

        for (IMovingBlock b : blocks) {
            for (int i = 0; i <= distance; i++)
                placeholder.put(b.getPosition().copy().offset(direction, i), true);
        }

        return placeholder;
    }
}
