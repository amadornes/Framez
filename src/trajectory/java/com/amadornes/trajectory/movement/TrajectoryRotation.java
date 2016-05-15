package com.amadornes.trajectory.movement;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.ITrajectory.ITrajectoryRotation;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.MathHelper;
import com.amadornes.trajectory.api.vec.Vector3;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TrajectoryRotation implements ITrajectory, ITrajectoryRotation {

    protected BlockPos origin;
    protected int axis, rotations;
    protected double speed;

    public TrajectoryRotation(BlockPos origin, int axis, int rotations, double speed) {

        this.origin = origin;
        this.axis = axis;
        this.rotations = rotations;
        this.speed = speed;
    }

    public TrajectoryRotation() {

    }

    @Override
    public Vector3 getOrigin() {

        return new Vector3(getOriginBlock()).add(Vector3.center);
    }

    @Override
    public BlockPos getOriginBlock() {

        return origin;
    }

    @Override
    public int getAxis() {

        return axis;
    }

    @Override
    public int getRotations() {

        return rotations;
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

        Vector3 origin = getOrigin().copy().sub(position);
        BlockPos axis = new BlockPos().offset(getAxis());
        GL11.glTranslated(origin.x, origin.y, origin.z);
        GL11.glRotated(Math.max(0, getProgress(ticks)) * getRotations() * 90 * (axis.x + axis.y + axis.z), Math.abs(axis.x),
                Math.abs(axis.y), Math.abs(axis.z));
        GL11.glTranslated(-origin.x, -origin.y, -origin.z);
    }

    @Override
    public Vector3 transformVec(Vector3 vector, float ticks) {

        Vector3 origin = getOrigin();
        BlockPos axis = BlockPos.sideOffsets[getAxis()];
        return vector
                .copy()
                .sub(origin)
                .rotate(getProgress(ticks) * getRotations() * 90 * MathHelper.torad * (axis.x + axis.y + axis.z), Math.abs(axis.x),
                        Math.abs(axis.y), Math.abs(axis.z)).add(origin);
    }

    @Override
    public BlockPos transformPos(BlockPos position) {

        BlockPos origin = getOriginBlock();
        int axis = getAxis();
        position = position.copy().sub(origin);
        if (axis == 0)
            position = new BlockPos(-position.z, position.y, position.x);
        else if (axis == 1)
            position = new BlockPos(position.z, position.y, -position.x);
        else if (axis == 2)
            position = new BlockPos(position.y, -position.x, position.z);
        else if (axis == 3)
            position = new BlockPos(-position.y, position.x, position.z);
        else if (axis == 4)
            position = new BlockPos(position.x, position.z, -position.y);
        else if (axis == 5)
            position = new BlockPos(position.x, -position.z, position.y);
        return position.add(origin);
    }

    @Override
    public AxisAlignedBB[] transformAABB(AxisAlignedBB aabb, float ticks) {

        Vector3 center = new Vector3((aabb.minX + aabb.maxX) / 2D, (aabb.minY + aabb.maxY) / 2D, (aabb.minZ + aabb.maxZ) / 2D);
        Vector3 transformed = transformVec(center, ticks);
        return new AxisAlignedBB[] { aabb.copy().offset(transformed.x - center.x, transformed.y - center.y, transformed.z - center.z) };
    }

    @Override
    public void transformBlock(IMovingBlock block) {

        for (int i = 0; i < rotations; i++)
            block.getBlock().rotateBlock(block.getFakeWorld(), block.getPosition().x, block.getPosition().y, block.getPosition().z,
                    ForgeDirection.getOrientation(axis ^ 1));
    }

    @Override
    public void moveEntity(Entity entity, float ticks) {

        Vector3 pos = transformVec(new Vector3(entity.posX, entity.posY, entity.posZ), 0.995F).sub(
                new Vector3(entity.posX, entity.posY, entity.posZ));
        entity.moveEntity(pos.x, pos.y + 0.005, pos.z);
        entity.onGround = true;

        if (!(entity instanceof EntityPlayer && entity.isSneaking()) && (axis == 0 || axis == 1)) {
            entity.prevRotationYaw = entity.rotationYaw;
            entity.rotationYaw += getSpeed() * getRotations() * 90 * ((axis % 2) == 0 ? 1 : -1);
        }
    }

    @Override
    public TrajectoryRotation copy() {

        return new TrajectoryRotation(origin.copy(), getAxis(), getRotations(), getSpeed());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("x", origin.x);
        tag.setInteger("y", origin.y);
        tag.setInteger("z", origin.z);

        tag.setInteger("axis", axis);
        tag.setInteger("rots", rotations);
        tag.setDouble("speed", speed);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        origin = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));

        axis = tag.getInteger("axis");
        rotations = tag.getInteger("rots");
        speed = tag.getDouble("speed");
    }

    @Override
    public void parseJSON(JsonElement json) throws Exception {

        JsonObject or = json.getAsJsonObject().get("origin").getAsJsonObject();
        origin = new BlockPos(or.get("x").getAsInt(), or.get("y").getAsInt(), or.get("z").getAsInt());

        axis = json.getAsJsonObject().get("axis").getAsInt();
        rotations = json.getAsJsonObject().get("rotations").getAsInt();
        speed = json.getAsJsonObject().get("speed").getAsDouble();

    }

    @Override
    public Map<BlockPos, Boolean> getPlaceholderPositions(BlockSet blocks) {

        Map<BlockPos, Boolean> placeholder = new HashMap<BlockPos, Boolean>();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (IMovingBlock b : blocks) {
            BlockPos t = transformPos(b.getPosition());
            minX = Math.min(minX, Math.min(b.getPosition().x, t.x));
            minY = Math.min(minY, Math.min(b.getPosition().y, t.y));
            minZ = Math.min(minZ, Math.min(b.getPosition().z, t.z));
            maxX = Math.max(maxX, Math.max(b.getPosition().x, t.x));
            maxY = Math.max(maxY, Math.max(b.getPosition().y, t.y));
            maxZ = Math.max(maxZ, Math.max(b.getPosition().z, t.z));
        }
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    placeholder.put(new BlockPos(x, y, z), false);
        for (IMovingBlock b : blocks) {
            BlockPos t = transformPos(b.getPosition());
            placeholder.put(b.getPosition(), true);
            placeholder.put(t, true);
        }

        return placeholder;
    }

}
