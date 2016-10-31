package com.amadornes.framez.movement;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import com.amadornes.framez.api.movement.IMovingStructure;
import com.amadornes.framez.api.movement.MovementIssue;
import com.amadornes.framez.util.AdvancedMutableBlockPos;
import com.amadornes.framez.util.Graph;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MovingStructure implements IMovingStructure {

    private final Map<MovingBlock, BlockPos> blocks;
    private final Set<MovementIssue> issues;

    private MovingStructure(Map<MovingBlock, BlockPos> blocks, Set<MovementIssue> issues) {

        this.blocks = blocks;
        this.issues = issues;
    }

    @Override
    public Map<MovingBlock, BlockPos> getBlocks() {

        return blocks;
    }

    @Override
    public Set<MovementIssue> getMovementIssues() {

        return issues;
    }

    public NBTTagCompound toNBT() {

        NBTTagCompound tag = new NBTTagCompound();

        // NBTTagList blocks = new NBTTagList();
        // for (MovingBlock b : getBlocks().keySet()) {
        //
        // }

        return tag;// TODO: Serialize to NBT
    }

    public static int getSizeAlongAxis(Collection<MovingBlock> blocks, Axis axis, BlockPos reference, int minVal, int maxVal) {

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, val;
        for (MovingBlock b : blocks) {
            BlockPos pos = b.getPos();
            if (axis == Axis.X) {
                if (pos.getY() != reference.getY() && pos.getZ() != reference.getZ()) {
                    continue;
                }
                val = pos.getX();
            } else if (axis == Axis.Y) {
                if (pos.getX() != reference.getX() && pos.getZ() != reference.getZ()) {
                    continue;
                }
                val = pos.getY();
            } else {
                if (pos.getX() != reference.getX() && pos.getY() != reference.getY()) {
                    continue;
                }
                val = pos.getZ();
            }
            if (val < minVal || val > maxVal) {
                continue;
            }
            min = Math.min(min, val);
            max = Math.max(max, val);
        }
        return max - min;
    }

    public static MovingStructure fromNBT(NBTTagCompound tag) {

        return null;// TODO: Deserialize
    }

    @SafeVarargs
    public static MovingStructure discover(BlockPos pos, BiPredicate<IBlockAccess, BlockPos> ignored, EnumSet<EnumFacing> directions,
            boolean isMotorSticky, Function<Set<MovingBlock>, IMovement> movementSupplier, Supplier<World>... worlds) {

        Graph<MovingBlock> graph = new Graph<MovingBlock>();
        Queue<MovingBlock> nodes = new ArrayDeque<MovingBlock>();
        AdvancedMutableBlockPos mutablePos = new AdvancedMutableBlockPos();

        MovingBlock start = new MovingBlock(worlds[0], pos, directions, isMotorSticky);
        nodes.add(start);
        graph.addVertex(start);

        MovingBlock node;
        while (!nodes.isEmpty()) {
            node = nodes.poll();
            for (EnumFacing face : EnumFacing.VALUES) {
                if (node.isSticky(face) && !graph.getVertices().contains(mutablePos.set(node.pos.offset(face)))) {
                    for (Supplier<World> world : worlds) {
                        if (!ignored.test(world.get(), mutablePos)) {
                            MovingBlock neighbor = new MovingBlock(world, mutablePos.toImmutable());
                            if (neighbor.canStickTo(face.getOpposite())) {
                                graph.addEdge(node, neighbor);
                                nodes.add(neighbor);
                            }
                            break;
                        }
                    }
                }
            }
        }

        Graph<MovingBlock> optimalPath = graph.getMST(start);
        Set<MovingBlock> blocks = optimalPath.getVertices();
        IMovement movement = movementSupplier.apply(blocks);
        Map<MovingBlock, BlockPos> blockMap = new HashMap<MovingBlock, BlockPos>();
        for (MovingBlock b : blocks) {
            if (b != start) {
                blockMap.put(b, movement != null ? movement.transform(b.getPos()) : b.getPos());
            }
        }

        // This is not needed. Motors may want to move even if there are no blocks (linear actuator)
        // if (blockMap.isEmpty()) return null;

        return new MovingStructure(blockMap, Collections.emptySet()); // TODO: Implement movement issues
    }

}
