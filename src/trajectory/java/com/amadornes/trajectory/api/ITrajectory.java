package com.amadornes.trajectory.api;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.api.vec.Vector3;
import com.google.gson.JsonElement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Interface that represents a trajectory.
 */
public interface ITrajectory {

    /**
     * Gets the origin (or center) of the movement.
     */
    public Vector3 getOrigin();

    /**
     * Gets the movement progress based on how many ticks have passed since it started.
     */
    public double getProgress(float ticks);

    /**
     * Performs an OpenGL transformation that will be applied to the RenderList containing the blocks.
     */
    @SideOnly(Side.CLIENT)
    public void transformGL(Vector3 position, BlockSet blocks, float ticks);

    /**
     * Transforms a {@link Vector3} based on how many ticks have passed since it started moving.
     */
    public Vector3 transformVec(Vector3 vector, float ticks);

    /**
     * Transforms a {@link BlockPos} to its final position.
     */
    public BlockPos transformPos(BlockPos position);

    /**
     * Transforms an {@link AxisAlignedBB} based on how many ticks have passed since it started moving.
     */
    public AxisAlignedBB[] transformAABB(AxisAlignedBB aabb, float ticks);

    /**
     * Transforms an {@link IMovingBlock} just before it gets placed in the world again. <br/>
     * Doesn't alter coordintates, just the block itself. Used for trajectories like rotation, that call
     * {@link Block#rotateBlock(World, int, int, int, ForgeDirection)} when they finish moving.
     */
    public void transformBlock(IMovingBlock block);

    /**
     * Moves an {@link Entity} based on how many ticks have passed since it started moving.
     */
    public void moveEntity(Entity entity, float ticks);

    /**
     * Creates a copy of this trajectory.
     */
    public ITrajectory copy();

    /**
     * Writes this trajectory to NBT. Used for syncing with the client.
     */
    public void writeToNBT(NBTTagCompound tag);

    /**
     * Reads the trajectory from NBT. Used for syncing with the server.
     */
    public void readFromNBT(NBTTagCompound tag);

    /**
     * Parses the trajectory from a JSON element. Used with commands.<br/>
     * Throw an exception if it didn't succeed and its message will be shown to the client that issued the command.
     */
    public void parseJSON(JsonElement json) throws Exception;

    /**
     * Gets the positions of the placeholders required for a set of blocks, and whether they are required or not.
     */
    public Map<BlockPos, Boolean> getPlaceholderPositions(BlockSet blocks);

    public static interface ITrajectoryTranslation extends ITrajectory {

        public BlockPos getTranslation();

        public double getSpeed();

    }

    public static interface ITrajectoryRotation extends ITrajectory {

        public BlockPos getOriginBlock();

        public int getAxis();

        public int getRotations();

        public double getSpeed();

    }

}
