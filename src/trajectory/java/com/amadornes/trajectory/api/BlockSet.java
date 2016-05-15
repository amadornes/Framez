package com.amadornes.trajectory.api;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.world.World;

import com.amadornes.trajectory.api.vec.BlockPos;

@SuppressWarnings("serial")
/**
 * Represents the set of blocks that form a structure. They must all be in the same world.
 */
public final class BlockSet extends HashSet<IMovingBlock> {

    private World world;
    private BlockSet cache;
    private boolean avoidCME = false;

    /**
     * Builds a BlockSet with a default world. This prevents blocks in other worlds from being added to this set.
     */
    public BlockSet(World world) {

        this(true);
        this.world = world;
    }

    /**
     * Builds a BlockSet without a default world. The world of the first block added will be the one used.
     */
    public BlockSet() {

        this(true);
    }

    private BlockSet(boolean addCache) {

        if (addCache)
            cache = new BlockSet(false);
    }

    @Override
    public boolean add(IMovingBlock e) {

        if (world == null)
            world = e.getWorld();
        else if (world != e.getWorld())
            throw new IllegalStateException(
                    "A mod is attempting to move blocks in multiple dimensions at the same time! This is not a valid action! Report this to the author!");

        return avoidCME ? cache.add(e) : super.add(e);
    }

    public boolean add(BlockPos e) {

        if (world == null)
            throw new IllegalStateException("No world is defined!");

        return add(TrajectoryAPI.instance().createBlock(world, e));
    }

    @Override
    public boolean addAll(Collection<? extends IMovingBlock> c) {

        boolean result = false;
        for (IMovingBlock b : c)
            result |= add(b);

        return result;
    }

    /**
     * Gets the world all the blocks in this set are in.
     */
    public World getWorld() {

        return world;
    }

    /**
     * Finds a block at a specific position, or null if there isn't one.
     */
    public IMovingBlock find(int x, int y, int z) {

        for (IMovingBlock b : this)
            if (b.getPosition().x == x && b.getPosition().y == y && b.getPosition().z == z)
                return b;

        return null;
    }

    /**
     * Useful method that makes the add() and addAll() methods work with a separate set. This allows for modification of the list whilst looping
     * through it. Call pushCached() after the loop is over to push all the blocks in the cache to the main set.
     */
    public void setAvoidCME(boolean avoid) {

        this.avoidCME = avoid;
    }

    /**
     * Pushes all the blocks in the cache to this set and clears the cache.
     */
    public void pushCached() {

        addAll(cache);
        cache.clear();
    }

    /**
     * Creates a new BlockSet in the specified world, with the specified block positions in it (as instances of BlockPos).
     */
    public static BlockSet of(World world, BlockPos... positions) {

        BlockSet set = new BlockSet(world);
        for (BlockPos p : positions)
            set.add(p);
        return set;
    }

    /**
     * Creates a new BlockSet in the specified world, with the specified block positions in it (as 3 separate ints).
     */
    public static BlockSet of(World world, int... positions) {

        BlockSet set = new BlockSet(world);
        for (int i = 0; i < positions.length - 2; i += 3)
            set.add(new BlockPos(positions[i + 0], positions[i + 1], positions[i + 2]));
        return set;
    }

}
