package com.amadornes.trajectory.movement;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.ITrajectory.ITrajectoryTranslation;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;
import com.amadornes.trajectory.util.MiscUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class TrajectoryTranslation implements ITrajectory, ITrajectoryTranslation {

    protected BlockPos translation;
    protected double speed;

    public TrajectoryTranslation(BlockPos translation, double speed) {

        this.translation = translation;
        this.speed = speed;
    }

    public TrajectoryTranslation() {

    }

    @Override
    public Vector3 getOrigin() {

        return Vector3.zero;
    }

    @Override
    public BlockPos getTranslation() {

        return translation;
    }

    @Override
    public double getSpeed() {

        return speed;
    }

    @Override
    public double getProgress(float ticks) {

        return Math.min(getSpeed() * ticks, 1);
    }

    @Override
    public void transformGL(Vector3 position, BlockSet blocks, float ticks) {

        double progress = Math.max(0, getProgress(ticks));
        BlockPos translation = getTranslation();
        GL11.glTranslated(translation.x * progress, translation.y * progress, translation.z * progress);
    }

    @Override
    public Vector3 transformVec(Vector3 vector, float ticks) {

        return vector.copy().add(new Vector3(getTranslation()).mul(getProgress(ticks)));
    }

    @Override
    public BlockPos transformPos(BlockPos position) {

        return position.copy().add(getTranslation());
    }

    @Override
    public AxisAlignedBB[] transformAABB(AxisAlignedBB aabb, float ticks) {

        double progress = getProgress(ticks);
        BlockPos translation = getTranslation();
        return new AxisAlignedBB[] { aabb.copy().offset(translation.x * progress, translation.y * progress, translation.z * progress) };
    }

    @Override
    public void transformBlock(IMovingBlock block) {

    }

    @Override
    public void moveEntity(Entity entity, float ticks) {

        BlockPos translation = getTranslation();
        double speed = getSpeed();
        if (translation.y > 0 && ticks <= 1)
            MiscUtils.moveEntity(entity, 0, speed, 0);
        MiscUtils.moveEntity(entity, translation.x * speed, translation.y > 0 ? translation.y * speed * (1 + speed) : 0, translation.z
                * speed);
        if (translation.y > 0 && (getProgress(ticks) >= 1 || getProgress(ticks) == 0))
            MiscUtils.moveEntity(entity, 0, speed * 2, 0);
        entity.onGround = entity.isCollided = entity.isCollidedVertically = true;
    }

    @Override
    public TrajectoryTranslation copy() {

        return new TrajectoryTranslation(getTranslation().copy(), getSpeed());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("x", translation.x);
        tag.setInteger("y", translation.y);
        tag.setInteger("z", translation.z);
        tag.setDouble("speed", speed);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        translation = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
        speed = tag.getDouble("speed");
    }

    @Override
    public void parseJSON(JsonElement json) throws Exception {

        JsonArray tr = json.getAsJsonObject().get("translation").getAsJsonArray();
        translation = new BlockPos(tr.get(0).getAsInt(), tr.get(1).getAsInt(), tr.get(2).getAsInt());
        speed = json.getAsJsonObject().get("speed").getAsDouble();
    }

    @Override
    public Map<BlockPos, Boolean> getPlaceholderPositions(BlockSet blocks) {

        Map<BlockPos, Boolean> placeholder = new HashMap<BlockPos, Boolean>();

        for (IMovingBlock b : blocks) {
            placeholder.put(b.getPosition(), true);
            placeholder.put(transformPos(b.getPosition()), true);
        }

        // TODO: Calculate the ray from the start to the end, locating all the blocks

        return placeholder;
    }

}
