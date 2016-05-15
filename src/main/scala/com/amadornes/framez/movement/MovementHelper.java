package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.world.World;

import com.amadornes.framez.api.movement.IFrame;
import com.amadornes.framez.api.movement.IMovement;
import com.amadornes.framez.api.movement.IMovementRegistry.IgnoreMode;
import com.amadornes.framez.api.movement.IMultiblockMovementHandler;
import com.amadornes.framez.api.movement.IStructureMovementHandler;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.init.FramezConfig;
import com.amadornes.framez.util.FramezUtils;
import com.amadornes.framez.util.Graph;
import com.amadornes.framez.util.Graph.INode;
import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.IMovingBlock;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.api.vec.BlockPos;

public class MovementHelper {

    public static BlockSet findMovedBlocks(World world, BlockPos position, IMovement movement, IgnoreMode ignoreMode, BlockPos... ignored) {

        return findMovedBlocksAdv(world, position, movement, ignoreMode, ignored).blocks;
    }

    public static MovementData findMovedBlocksAdv(World world, BlockPos position, IMovement movement, IgnoreMode ignoreMode,
            BlockPos... ignored) {

        BlockSet blocks = new BlockSet(world);
        Graph<BlockNodeWrapper> graph = new Graph<BlockNodeWrapper>();

        BlockNodeWrapper first = new BlockNodeWrapper(world, position);

        Set<BlockNodeWrapper> currentPass = new HashSet<BlockNodeWrapper>();
        Set<BlockNodeWrapper> nextPass = new HashSet<BlockNodeWrapper>();
        currentPass.add(first);
        do {
            Iterator<BlockNodeWrapper> iterator = currentPass.iterator();
            while (iterator.hasNext()) {
                BlockNodeWrapper c = iterator.next();

                // Loop through all the neighbours
                for (BlockPos b : findNeighbours(world, c.position, movement)) {
                    BlockNodeWrapper w = new BlockNodeWrapper(world, b);
                    IMovingBlock mb = TrajectoryAPI.instance().createBlock(world, b);
                    if (!nextPass.contains(w) && !currentPass.contains(w) && !blocks.contains(mb)
                            && !(FramezUtils.contains(ignored, b) && ignoreMode == IgnoreMode.AVOID)) {
                        nextPass.add(w);
                        blocks.add(mb);
                        graph.addEdge(c, w);
                    }
                }

                // Empty the list as we go
                iterator.remove();
            }

            Set<BlockNodeWrapper> next = currentPass;// At this point we have an empty list, save it for the next pass
            currentPass = nextPass;// Assign the things in the next pass to the current one
            nextPass = next;// Assign the empty list to next pass

            if (ignoreMode == IgnoreMode.AVOID) {
                currentPass.removeAll(Arrays.asList(ignored));
            }
        } while (currentPass.size() > 0);

        Graph<BlockNodeWrapper> mst = graph.getMST(first);

        BlockSet actuallyMoved = new BlockSet(world);
        for (BlockNodeWrapper w : mst.getVertices())
            if (!FramezUtils.contains(ignored, w.position))
                actuallyMoved.add(w.position);

        actuallyMoved.setAvoidCME(true);
        for (IMultiblockMovementHandler handler : MovementRegistry.instance.multiblockMovementHandlers)
            handler.addBlocks(world, actuallyMoved);
        actuallyMoved.setAvoidCME(false);
        actuallyMoved.pushCached();

        return new MovementData(actuallyMoved, graph, mst);
    }

    private static List<BlockPos> findNeighbours(World world, BlockPos position, IMovement movement) {

        List<BlockPos> l = new ArrayList<BlockPos>();
        for (int i = 0; i < 6; i++)
            if (canStick(world, position, i, movement))
                l.add(position.copy().offset(i));

        return l;
    }

    public static Set<MovementIssue> getMovementIssues(MovementData data, IMovement movement) {

        World world = data.blocks.getWorld();
        Set<MovementIssue> issues = new HashSet<MovementIssue>();

        if (!TrajectoryAPI.instance().canMove(data.blocks, movement)) {
            issues.add(MovementIssue.BLOCK.at(new BlockPos()).ofColor(0x000000).withInformation("<ERROR>"));
            return issues;
        }

        for (Entry<BlockPos, Boolean> e : movement.getPlaceholderPositions(data.blocks).entrySet()) {
            if (data.blocks.find(e.getKey().x, e.getKey().y, e.getKey().z) != null)
                continue;
            if (e.getValue() && !MovementRegistry.instance.canBeOverriden(data.blocks.getWorld(), e.getKey())) {
                issues.add(MovementIssue.BLOCK.at(e.getKey()).ofColor(FramezConfig.color_issue_in_the_way)
                        .withInformation("framez:issue.inTheWay"));
            }
        }

        for (BlockNodeWrapper attempt : data.graph.getVertices())
            if (!data.mst.getVertices().contains(attempt))
                issues.add(MovementIssue.BLOCK.at(attempt.position).ofColor(FramezConfig.color_issue_too_many_neighbours)
                        .withInformation("framez:issue.manyNeighbours"));

        for (IMovingBlock b : data.blocks) {
            IFrame frame = MovementRegistry.instance.getFrameAt(world, b.getPosition());
            if (frame != null)
                if (frame.getMultipartCount() > frame.getMaterial().getMaxMultiparts())
                    issues.add(MovementIssue.BLOCK.at(b.getPosition()).ofColor(FramezConfig.color_issue_multiparts)
                            .withInformation("framez:issue.multiparts"));

            for (IStructureMovementHandler handler : MovementRegistry.instance.structureMovementHandlers)
                handler.addIssues(data.blocks, movement, issues);
        }

        return issues;
    }

    public static boolean canStick(World world, BlockPos position, int side, IMovement movement) {

        if (!TrajectoryAPI.instance().canBeMoved(TrajectoryAPI.instance().createBlock(world, position.copy().offset(side)).snapshot(),
                movement))
            return false;
        if (TrajectoryAPI.instance().isMoving(world, position))
            return false;
        if (!MovementRegistry.instance.isSideSticky(world, position, side, movement))
            return false;
        if (!MovementRegistry.instance.canStickToSide(world, position.copy().offset(side), side ^ 1, movement))
            return false;
        if (MovementRegistry.instance.getSideStickiness(world, position, side) < MovementRegistry.instance.getRequiredStickiness(world,
                position.copy().offset(side), side ^ 1))
            return false;

        return true;
    }

    public static class BlockNodeWrapper implements INode {

        private World world;
        private BlockPos position;

        public BlockNodeWrapper(World world, BlockPos position) {

            this.world = world;
            this.position = position;
        }

        @Override
        public int getMaxNeighbors() {

            IFrame frame = MovementRegistry.instance.getFrameAt(world, position);
            if (frame != null)
                return frame.getMaterial().getMaxMovedBlocks();
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean equals(Object o) {

            if (o instanceof BlockNodeWrapper)
                return position.equals(((BlockNodeWrapper) o).position);
            if (o instanceof IMovingBlock)
                return position.equals(((IMovingBlock) o).getPosition());

            return position.equals(o);
        }

        @Override
        public int hashCode() {

            return position.hashCode();
        }

    }

    public static class MovementData {

        public BlockSet blocks;
        public Graph<BlockNodeWrapper> graph, mst;

        public MovementData(BlockSet blocks, Graph<BlockNodeWrapper> graph, Graph<BlockNodeWrapper> mst) {

            this.blocks = blocks;
            this.graph = graph;
            this.mst = mst;
        }

    }

}
