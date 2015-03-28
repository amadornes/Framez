package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.vec.Vec3i;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovable;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IStickinessHandler;
import com.amadornes.framez.api.movement.ISticky;
import com.amadornes.framez.init.FramezBlocks;
import com.amadornes.framez.tile.TileMoving;
import com.amadornes.framez.util.Graph;

public class MovementHelper {

    public static Pair<List<MovingBlock>, List<Vec3i>> findMovedBlocks(World world, int x, int y, int z, ForgeDirection side,
            IMovement movement) {

        return findMovedBlocks(world, x, y, z, side, movement, new ArrayList<Vec3i>());
    }

    public static Pair<List<MovingBlock>, List<Vec3i>> findMovedBlocks(World world, int x, int y, int z, ForgeDirection side,
            IMovement movement, List<Vec3i> ignored) {

        List<MovingBlock> moved = new ArrayList<MovingBlock>();
        Graph<MovingBlock> graph = new Graph<MovingBlock>();

        MovingBlock firstBlock = findBlock(world, x, y, z, side, movement);
        if (firstBlock == null)
            return new Pair<List<MovingBlock>, List<Vec3i>>(moved, new ArrayList<Vec3i>());
        moved.add(firstBlock);

        List<MovingBlock> current = new ArrayList<MovingBlock>();
        List<MovingBlock> tmp = new ArrayList<MovingBlock>();

        current.add(firstBlock);

        while (current.size() > 0) {
            for (MovingBlock b : current) {
                for (MovingBlock bl : findBlocks(b.getWorld(), b.getX(), b.getY(), b.getZ(), movement, moved, ignored)) {
                    bl.snapshot();
                    graph.addEdge(b, bl);
                    if (!tmp.contains(bl) && !current.contains(bl) && !moved.contains(bl)) {
                        tmp.add(bl);
                        moved.add(bl);
                    }
                }
            }

            current.clear();
            current.addAll(tmp);
            tmp.clear();
        }

        current.clear();

        Graph<MovingBlock> mst = graph.getMST(firstBlock);
        List<MovingBlock> actuallyMoved = new ArrayList<MovingBlock>();
        for (MovingBlock b : mst.getVertices())
            if (!actuallyMoved.contains(b))
                actuallyMoved.add(b);

        List<MovingBlock> notMovedBlocks = new ArrayList<MovingBlock>(moved);
        notMovedBlocks.removeAll(actuallyMoved);

        List<Vec3i> notMoved = new ArrayList<Vec3i>();
        notMoved.addAll(getBlocksInTheWay(moved, movement));

        for (MovingBlock b : notMovedBlocks)
            if (!notMoved.contains(movement.transform(new Vec3i(b))))
                notMoved.add(new Vec3i(b));

        notMovedBlocks.clear();
        moved.clear();

        return new Pair<List<MovingBlock>, List<Vec3i>>(actuallyMoved, notMoved);
    }

    public static MovingBlock findBlock(World world, int x, int y, int z, ForgeDirection side, IMovement movement) {

        List<IMovable> movableList = FrameMovementRegistry.instance().findMovables(world, x, y, z);
        boolean canMove = movableList.size() == 0;
        for (IMovable movable : movableList) {
            Priority p = null;
            try {
                p = movable.getClass()
                        .getMethod("getMovementType", World.class, int.class, int.class, int.class, ForgeDirection.class, IMovement.class)
                        .getAnnotation(Priority.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (p != null && p.value() == PriorityEnum.OVERRIDE) {
                BlockMovementType type = movable.getMovementType(world, x, y, z, side, movement);
                if (type != null) {
                    if (!type.isMovable())
                        return null;
                    return new MovingBlock(new Vec3i(x, y, z, world), null, FrameMovementRegistry.instance().findFrames(world, x, y, z));
                }
            }
            BlockMovementType type = movable.getMovementType(world, x, y, z, side, movement);
            if (type != null)
                canMove |= type.isMovable();
        }

        if (!canMove)
            return null;

        return new MovingBlock(new Vec3i(x, y, z, world), null, FrameMovementRegistry.instance().findFrames(world, x, y, z));
    }

    public static List<MovingBlock> findBlocks(World world, int x, int y, int z, IMovement movement, List<MovingBlock> alreadyMoving,
            List<Vec3i> ignored) {

        List<MovingBlock> blocks = new ArrayList<MovingBlock>();

        List<ISticky> stickies = FrameMovementRegistry.instance().findStickies(world, x, y, z);
        for (ISticky sticky : stickies) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                boolean handled = false;
                boolean isSticky = false;

                for (IStickinessHandler h : FrameMovementRegistry.instance().getStickinessHandlers()) {
                    if (h.canHandle(world, x, y, z, d)) {
                        handled = true;
                        isSticky = h.isSideSticky(world, x, y, z, d, movement);
                        break;
                    }
                }

                if (handled && !isSticky)
                    continue;
                if (!handled && !sticky.isSideSticky(world, x, y, z, d, movement))
                    continue;

                MovingBlock b = findBlock(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite(), movement);
                if (b != null && !blocks.contains(b)) {
                    boolean ignore = false;
                    for (Vec3i v : ignored) {
                        if (b.equals(v)) {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore)
                        blocks.add(b);
                }
            }
        }

        return blocks;
    }

    public static List<Vec3i> getBlocksInTheWay(List<MovingBlock> movingBlocks, IMovement movement) {

        List<Vec3i> blocks = new ArrayList<Vec3i>();
        for (MovingBlock b : movingBlocks)
            blocks.add(new Vec3i(b));

        List<Vec3i> moved = new ArrayList<Vec3i>();
        for (Vec3i v : blocks)
            moved.add(movement.transform(v));

        moved.removeAll(blocks);

        List<Vec3i> blocking = new ArrayList<Vec3i>();

        for (Vec3i v : moved) {
            if (v.getBlock() == FramezBlocks.moving) {
                TileEntity te = v.getTileEntity();
                // Skip moving blocks, they'll disappear before the structure starts moving
                if (te == null
                        || (te instanceof TileMoving && (((TileMoving) te).getBlockA() == null || ((TileMoving) te).getBlockB() == null)))
                    continue;
            }
            List<IMovable> movableList = FrameMovementRegistry.instance().findMovables(v.getWorld(), v.getX(), v.getY(), v.getZ());
            for (IMovable movable : movableList) {
                Priority p = null;
                try {
                    p = movable
                            .getClass()
                            .getMethod("getMovementType", World.class, int.class, int.class, int.class, ForgeDirection.class,
                                    IMovement.class).getAnnotation(Priority.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BlockMovementType type = movable.getMovementType(v.getWorld(), v.getX(), v.getY(), v.getZ(), ForgeDirection.UNKNOWN,
                        movement);
                if (type != null) {
                    if (!type.isReplaceable()) {
                        blocking.add(v);
                        break;
                    }
                    if (p != null && p.value() == PriorityEnum.OVERRIDE)
                        break;
                }
            }
        }

        return blocking;
    }

}
